package com.it.search.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

/**
 * @description:zk配置类
 * @author: Delusion
 * @date: 2021-04-06 8:31
 */

@Configuration
public class ZookeeperConfig {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);

    @Value("${zookeeper.address}")
    private String connectString;

    @Value("${zookeeper.timeout}")
    private int timeout;


    @Bean
    public ZooKeeper zkClient() {
        ZooKeeper zooKeeper = null;
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            //连接成功后，会回调watcher监听，此连接操作是异步的，执行完new语句后，直接调用后续代码
            //  可指定多台服务地址 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
            zooKeeper = new ZooKeeper(connectString, timeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (Event.KeeperState.SyncConnected == event.getState()) {
                        //如果收到了服务端的响应事件,连接成功
                        countDownLatch.countDown();
                    }
                    String path = event.getPath();
                    if (event.getState() == Event.KeeperState.Expired) {
                        System.out.println("session超时,zookeeper服务器连接失败");
                    } else if ((event.getState() == Event.KeeperState.SyncConnected) ||
                            event.getState() == Event.KeeperState.ConnectedReadOnly) {
                        System.out.println("zookeeper已连接成功");
                        countDownLatch.countDown();//连接成功后，主线程开始执行
                        if (event.getType() == Event.EventType.NodeCreated) {
                            System.out.println("zookeeper有新节点创建" + event.getPath());
                        }
                        if (event.getType() == Event.EventType.NodeDataChanged) {
                            System.out.println("zookeeper有节点数据变化" + event.getPath());
                        }
                        if (event.getType() == Event.EventType.NodeDeleted) {
                            System.out.println("zookeeper有节点被删除" + event.getPath());
                        }
                        if (event.getType() == Event.EventType.NodeChildrenChanged) {
                            System.out.println("zookeeper有子节点变化" + event.getPath());
                        }
                    } else {
                        System.out.println("zookeeper 未判断异常 path " + path + "  event" + event);
                    }
                }
            });
            countDownLatch.await();
            logger.info("【初始化ZooKeeper连接状态....】={}", zooKeeper.getState());

        } catch (Exception e) {
            logger.error("初始化ZooKeeper连接异常....】={}", e);
        }
        return zooKeeper;
    }
}
