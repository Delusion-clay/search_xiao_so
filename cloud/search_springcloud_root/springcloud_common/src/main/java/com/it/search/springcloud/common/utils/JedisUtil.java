package com.it.search.springcloud.common.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.net.SocketTimeoutException;
import java.util.Properties;


public class JedisUtil {

       private static Properties redisConf;

       //配置文件路径
       private static final String redisConfPath = "redis.properties";
       //静态代码块加载配置文件
       static {
              redisConf = ConfigUtil.getInstance().getProperties(redisConfPath);
       }

       public static Jedis getJedis(int db){
             Jedis jedis = JedisUtil.getJedis();
             if(jedis!=null){
                    jedis.select(db);
             }
             return jedis;
       }


       public static void close(Jedis jedis){
              if(jedis!=null){
                     jedis.close();
              }
       }

       /**
        * 并发很高的时候 会出现获取连接失败的情况  导致程序挂掉
        * @return
        */
       public static Jedis getJedis(){
              int timeoutCount = 0;
              while (true) {// 如果是网络超时则多试几次
                     try
                     {
                            Jedis jedis = new Jedis(redisConf.get("redis.hostname").toString(),
                                    Integer.valueOf(redisConf.get("redis.port").toString()));
                            return jedis;
                     } catch (Exception e)
                     {
                            if (e instanceof JedisConnectionException || e instanceof SocketTimeoutException)
                            {
                                   timeoutCount++;
                                   if (timeoutCount > 4)
                                   {
                                          break;
                                   }
                            }else
                            {
                                   break;
                            }
                     }
              }
              return null;
       }


       public static void main(String[] args) {
              //HASH
              Jedis jedis = JedisUtil.getJedis(10);
              jedis.hset("111","name","1");
              jedis.hset("111","age","7");
              JedisUtil.close(jedis);
       }




}
