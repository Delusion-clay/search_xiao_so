package com.it.search.crawler;

import com.alibaba.fastjson.JSON;
import com.it.search.bean.JobInfo;
import com.it.search.config.ZookeeperConfig;
import com.it.search.service.IMailService;
import com.it.search.util.HttpUtils;
import com.it.search.util.JedisUtil;
import com.lou.simhasher.SimHasher;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.UUID;

/**
 * @description:
 * @author: Delusion
 * @date: 2021-05-24 16:40
 */
@Component
public class DataCrawler {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);

    @Autowired
    private ZooKeeper zkClient;

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Resource
    private IMailService iMailService;


    //@Scheduled(cron = "* * * * * ?")
    @Scheduled(initialDelay = 1000,fixedDelay = 1000*60*60*24)
    public  void Crawling() throws Exception {
        Jedis jedis = JedisUtil.getJedis(1);
        logger.info("爬虫任务执行");
        String ip = InetAddress.getLocalHost().getHostAddress();
        Stat stat = zkClient.exists("/spider/" + ip, false);
        if (stat == null) {
            zkClient.create("/spider/" + ip, ip.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            iMailService.sendSimpleMail("927486507@qq.com","爬虫节点上线","爬虫节点上线,节点IP为："+ip);

        }
        logger.info("爬虫节点上线执行");
        jedis.del("url_set");
        String type = "";
        for (int i = 1; i < 15; i++) {
            String url = "https://www.xiaoso.net/f/47/start/" + i + "00";
            jedis.sadd("url_set", url);

        }
        while (true) {
            if (jedis.scard("url_set") != 0) {
                //爬取指定页面
                String url = jedis.spop("url_set");
                System.out.println(url);
                String html = HttpUtils.getHtml(url);
                Document doc = Jsoup.parse(html);
                Elements nodes = doc.select("div.threadlist_content");
                for (Element node : nodes) {
                    //获取数据
                    String title = node.select("div.threadlist_subject a").text();
                    String titleUrl = node.select("div.threadlist_subject a").attr("href");
                    String dec = node.select("div.small").text();
                    String[] types = dec.split(" / ");
                    //分类数据处理
                    if (types[0].contains("水木")) {
                        type = "水木";
                    } else if (types[0].contains("抽屉")) {
                        type = "抽屉";
                    } else {
                        type = types[0];
                    }
                    SimHasher simHasher = new SimHasher(title);
                    BigInteger signature = simHasher.getSignature();
                    if (!jedis.exists(String.valueOf(signature))){
                        jedis.hset(String.valueOf(signature),"publisher",StringUtils.isNotBlank(String.valueOf(signature))?String.valueOf(signature):"");
                        JobInfo info = new JobInfo();
                        info.setId(UUID.randomUUID().toString().replace("-", ""));
                        info.setType(type);
                        info.setTitle(title);
                        info.setUrl(titleUrl);
                        info.setInfomessage(dec);
                        String infoJson = JSON.toJSON(info).toString();
                        System.out.println(infoJson);
                        //kafkaTemplate.send("xiao_so_succ", infoJson);
                    }
                }
            } else {
                break;
            }
        }
        logger.error("=========================今日爬虫任务结束=======================");
    }

}
