package com.it.search.springcloud.common.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @description:
 * @author: Delusion
 * @date: 2021-03-20 15:37
 */
public class ConfigUtil {
    private static volatile ConfigUtil configUtil;

    private ConfigUtil() {
    }

    //3.提供一个对外的公共访问接口ss
    public static ConfigUtil getInstance() {
        //双重否定
        if (configUtil == null) {
            synchronized (ConfigUtil.class) {
                if (configUtil == null) {
                    configUtil = new ConfigUtil();
                }
            }
        }
        return configUtil;
    }
    public Properties getProperties(String path){
        Properties properties = new Properties();
        try {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(path);
            properties.load(resourceAsStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        return properties;
    }
}
