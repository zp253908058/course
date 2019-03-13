package com.zp.course.util.log;

import android.util.Log;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see Logger
 * @since 2019/3/12
 */
public class Logger {

    private static final String TAG = "";

    public static void d(String message, Object... args) {
        Log.d(TAG, message);
    }

    public static void d(Object object) {
        Log.d(TAG, object.toString());
    }

    public static void e(String message, Object... args) {
        Log.e(TAG, message);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        Log.e(TAG, message);
    }

    public static void i(String message, Object... args) {
        Log.i(TAG, message);
    }

    public static void v(String message, Object... args) {
        Log.v(TAG, message);
    }

    public static void w(String message, Object... args) {
        Log.w(TAG, message);
    }

    public static void wtf(String message, Object... args) {
        Log.d(TAG, message);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        Log.d(TAG, json);
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        Log.d(TAG, xml);
    }

    /**
     * print current time millisecond.
     */
    public static void millis(){
        Log.d(TAG, String.valueOf(System.currentTimeMillis()));
    }
}
