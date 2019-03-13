package com.zp.course.util;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see Validator
 * @since 2019/3/11
 */
public class Validator {

    /**
     * regex string to match number.
     */
    private static final String REGEX_NUMERIC = "-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?";

    /**
     * regex string to match mobile number.
     * <p>
     * 移动：134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     * 联通：130,131,132,145,152,155,156,1709,171,176,185,186
     * 电信：133,134,153,1700,177,180,181,189
     */
    private static final String REGEX_MOBILE_NUMBER = "^1([378][0-9]|4[57]|5[0-35-9])\\d{8}$";

    private Validator() {
        Preconditions.checkArgument(false, "this method could not be invoke!");
    }

    /**
     * 判断对象是否为空
     *
     * @param reference reference
     * @param <T>       type
     * @return true null.
     */
    public static <T> boolean isNull(@Nullable T reference) {
        return reference == null;
    }

    /**
     * 判断对象是否不为空
     *
     * @param reference reference
     * @param <T>       type
     * @return true not null.
     */
    public static <T> boolean isNotNull(@Nullable T reference) {
        return reference != null;
    }

    /**
     * 判断集合是否为null/空.
     *
     * @param collection collection
     * @param <E>        type
     * @return true null or empty.
     */
    public static <E> boolean isEmpty(@Nullable Collection<E> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否不为空.
     *
     * @param collection collection
     * @param <E>        type
     * @return true not empty.
     */
    public static <E> boolean isNotEmpty(@Nullable Collection<E> collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * 判断Map是否为null/空.
     *
     * @param map map
     * @param <K> type of key.
     * @param <V> type of value.
     * @return true null or empty.
     */
    public static <K, V> boolean isEmpty(@Nullable Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断Map是否不为null/空.
     *
     * @param map map
     * @param <K> type of key.
     * @param <V> type of value.
     * @return true not empty.
     */
    public static <K, V> boolean isNotEmpty(@Nullable Map<K, V> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * 判断字符串是否为null/empty.
     *
     * @param str string
     * @return true null or empty.
     */
    public static boolean isEmpty(@Nullable String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为null/empty.
     *
     * @param str string
     * @return true not empty.
     */
    public static boolean isNotEmpty(@Nullable String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * 判断字符串是否为null/empty.
     *
     * @param charSequence CharSequence
     * @return true null or empty.
     */
    public static boolean isEmpty(@Nullable CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    /**
     * 判断字符串是否不为null/empty.
     *
     * @param charSequence CharSequence
     * @return true not empty.
     */
    public static boolean isNotEmpty(@Nullable CharSequence charSequence) {
        return charSequence != null && charSequence.length() > 0;
    }

    /**
     * 判断数据是否为null/empty
     *
     * @param t   数组
     * @param <T> type
     * @return true null or empty.
     */
    public static <T> boolean isEmpty(@Nullable T[] t) {
        return t == null || t.length == 0;
    }

    /**
     * 判断数据是否不为null/empty
     *
     * @param t   数组
     * @param <T> type
     * @return true not empty.
     */
    public static <T> boolean isNotEmpty(@Nullable T[] t) {
        return t != null && t.length > 0;
    }

    /**
     * 判断字符串是否为数字
     *
     * @param value value
     * @return true numeric
     */
    public static boolean isNumeric(String value) {
        if (isEmpty(value)) {
            return false;
        }
        return value.matches(REGEX_NUMERIC);
    }

    /**
     * 验证手机号码
     *
     * @param mobiles mobiles
     * @return true is mobile number.
     */
    public static boolean isMobileNumber(String mobiles) {
        Pattern pattern = Pattern.compile(REGEX_MOBILE_NUMBER);
        Matcher matcher = pattern.matcher(mobiles);
        return matcher.matches();
    }
}
