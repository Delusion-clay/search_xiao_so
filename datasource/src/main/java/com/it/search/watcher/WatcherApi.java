package com.it.search.watcher;

import lombok.SneakyThrows;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: Delusion
 * @date: 2021-05-31 17:25
 */
//@Component
//@SpringBootTest
public class WatcherApi  {

    @Resource
    private ZooKeeper zkClient;

    private static final Logger logger = LoggerFactory.getLogger(WatcherApi.class);

    @Test
    public void process() throws InterruptedException, KeeperException {
        List<String> childrens = zkClient.getChildren("/spider", true);
        if (childrens!=null || childrens.size()!=0){
            for (String children : childrens) {
                System.out.println(children);
            }
        }
    }

    private String  doProcess(final String path, final Stat stat) throws KeeperException, InterruptedException {
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    // 核心思想就是这步，方法调方法
                    String innerResult = doProcess(path,stat);

                    String[] addressArray = innerResult.split(",");

                    for (String address : addressArray) {
                        System.err.println("ping " + address);
                    }
                } catch (KeeperException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        byte[] data = zkClient.getData(path, watcher, stat);

        return new String(data);
    }
}


