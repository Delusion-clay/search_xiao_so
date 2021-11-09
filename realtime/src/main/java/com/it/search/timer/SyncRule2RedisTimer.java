package com.it.search.timer;

import com.it.search.dao.RuleDao;
import com.it.search.domain.RuleDomain;
import com.it.search.utils.JedisUtil;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @description:
 * @author: Delusion
 * @date: 2021-05-31 16:03
 */
public class SyncRule2RedisTimer extends TimerTask {
    @Override
    public void run() {
        System.out.println("定时任务开始执行");
        //TODO 1.获取所有的规则
        List<RuleDomain> ruleList = RuleDao.getRuleList();
        //TODO 2 将规则写入到redis中
        Jedis jedis = null;
        try {
            for (int i = 0; i < ruleList.size(); i++) {
                jedis = JedisUtil.getJedis(15);
                RuleDomain tz_ruleDomain = ruleList.get(i);
                //获取对象参数
                String id = tz_ruleDomain.getId()+"";
                String words= tz_ruleDomain.getWords();
                //REDIS的key怎么设置
                //KEY必须唯一  warn_fieldname+"_"+warn_fieldvalue
                //String redisKey = id+":"+words;
                String redisKey = words;
                //jedis.hset(redisKey,"id", StringUtils.isNotBlank(id)?id:"");
                jedis.hset(redisKey,"publisher",StringUtils.isNotBlank(words)?words:"");
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JedisUtil.close(jedis);
        }

        System.out.println("===============同步规则成功======================="+ruleList.size());
    }
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new SyncRule2RedisTimer(),0,1*3*1000);
    }
}
