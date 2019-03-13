package com.zp.course.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see ObjectUtils
 * @since 2019/3/12
 */
public class ObjectUtils {

    public static List<Field> getDeclaredFields(Object object) {
        return Arrays.asList(object.getClass().getDeclaredFields());
    }

    public static List<Field> getAllDeclaredFields(Object object) {
        List<Field> list = new ArrayList<>();
        Class clz = object.getClass();
        if (clz == Object.class) {
            throw new IllegalArgumentException("object can not be instance of Object.class");
        }
        Field[] fields = clz.getDeclaredFields();
        do {
            if (Validator.isNotEmpty(fields)) {
                addToList(list, fields);
            }
            clz = clz.getSuperclass();
            if (clz == null) {
                break;
            }
        } while (clz != Object.class);
        return list;
    }

    private static <T> void addToList(List<T> list, T[] ts) {
        Collections.addAll(list, ts);
    }

    public static String toString(Object o) {
        return Validator.isNull(o) ? "null" : o.toString();
    }

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static boolean contains(String a, String b) {
        if (Validator.isEmpty(b)) {
            return true;
        }
        return Validator.isNotNull(a) && a.toLowerCase().contains(b.toLowerCase());
    }

    public static String getString(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }

    public static String concat(Object... objects) {
        String result = "";
        if (Validator.isNotEmpty(objects)) {
            for (Object object : objects) {
                String value = object == null ? "" : object.toString();
                result = result.concat(value);
            }
        }
        return result;
    }
}
