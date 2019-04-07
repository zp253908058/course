package com.zp.course.util;

import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see DateTimeUtils
 * @since 2019/3/20
 */
public class DateTimeUtils {

    private static final SimpleDateFormat DEFAULT_DATE_TIME_FORMAT;
    private static final SimpleDateFormat DEFAULT_DATE_FORMAT;
    private static final SimpleDateFormat DEFAULT_TIME_FORMAT;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DEFAULT_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault(Locale.Category.FORMAT));
            DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault(Locale.Category.FORMAT));
            DEFAULT_TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault(Locale.Category.FORMAT));
        } else {
            DEFAULT_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            DEFAULT_TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
        }
    }

    private DateTimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param millis     毫秒数
     * @param dateFormat 格式转换器
     * @return 指定格式的字符串
     */
    public static String getFormatString(long millis, SimpleDateFormat dateFormat) {
        return dateToString(new Date(millis), dateFormat);
    }

    /**
     * String to long time
     *
     * @param dateTime   带格式的时间字符串
     * @param dateFormat 格式转换器
     * @return 毫秒数
     */
    public static long getFormatMills(String dateTime, SimpleDateFormat dateFormat) {
        try {
            return dateFormat.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_TIME_FORMAT}
     *
     * @param millis 毫秒数
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime(long millis) {
        return getFormatString(millis, DEFAULT_DATE_TIME_FORMAT);
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_TIME_FORMAT}
     *
     * @return 返回当前时间的 yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentTimeInString() {
        return getDateTime(System.currentTimeMillis());
    }

    /**
     * get current time in milliseconds
     *
     * @return 返回当前时间的指定格式
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getFormatString(System.currentTimeMillis(), dateFormat);
    }

    /**
     * 返回带格式的日期
     *
     * @param millis 毫秒数
     * @return yyyy-MM-dd
     */
    public static String getDate(long millis) {
        return getFormatString(millis, DEFAULT_DATE_FORMAT);
    }

    /**
     * 返回带格式的时间
     *
     * @param millis 毫秒数
     * @return HH:mm
     */
    public static String getTime(long millis) {
        return getFormatString(millis, DEFAULT_TIME_FORMAT);
    }

    /**
     * 获取时间毫秒数
     *
     * @param time 格式化的时间
     * @return 毫秒数
     */
    public static long timeToMills(String time) {
        return getFormatMills(time, DEFAULT_TIME_FORMAT);
    }

    public static String dateToString(Date date, SimpleDateFormat format) {
        return format.format(date);
    }

    public static String dateToString(Date date) {
        return dateToString(date, DEFAULT_DATE_TIME_FORMAT);
    }
}
