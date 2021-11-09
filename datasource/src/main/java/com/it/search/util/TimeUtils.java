package com.it.search.util;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * Desc 时间工具类
 */
public abstract class TimeUtils {
    public static String format(Long timestamp,String pattern){
        return FastDateFormat.getInstance(pattern).format(timestamp);
    }


}
