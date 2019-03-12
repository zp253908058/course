package com.zp.course.util;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see StringUtils
 * @since 2019/3/11
 */
public class StringUtils {

    /**
     * 判断字符串为空
     *
     * @param cs CharSequence
     * @return true 为空， false 不为空
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 判断字符串不为空
     *
     * @param cs CharSequence
     * @return true 不为空， false 为空
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return cs != null && cs.length() > 0;
    }

    /**
     * 获取字符串长度
     *
     * @param cs CharSequence
     * @return 字符串长度，对象为空返回0
     */
    public static int lenth(CharSequence cs) {
        return cs != null ? cs.length() : 0;
    }
}
