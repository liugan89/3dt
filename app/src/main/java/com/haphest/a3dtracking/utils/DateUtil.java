package com.haphest.a3dtracking.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间相关工具类
 */
public class DateUtil {


    public static String toDateTime(long t) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(t));
    }

    public static String toYMDTime(long t) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd");
        return format.format(new Date(t));
    }

    public static String toDate(long t) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
        return format.format(new Date(t));
    }

    public static long YMDToLong(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd");
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String toSimpleDateTime(long t) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date(t));
    }

}
