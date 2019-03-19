package com.zp.course.util;


import androidx.annotation.Nullable;

/**
 * Class description
 *
 * @author zp
 * @version 1.0
 * @see Preconditions
 * @since 2017/5/3
 */
public class Preconditions {
    private Preconditions() {
        throw new IllegalArgumentException("Can not be instantiated!");
    }

    /**
     * 检查参数的合法性
     *
     * @param expression 布尔表达式
     * @throws IllegalArgumentException 表达式不成立
     */
    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 检查参数的合法性
     *
     * @param expression   布尔表达式
     * @param errorMessage 错误提示信息
     * @throws IllegalArgumentException 表达式不成立
     */
    public static void checkArgument(boolean expression, @Nullable Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    /**
     * 检查参数的合法性
     *
     * @param expression           布尔表达式
     * @param errorMessageTemplate 错误提示信息模板
     * @param errorMessageArgs     替换的内容
     * @throws IllegalArgumentException 表达式不成立
     * @throws NullPointerException     errorMessageTemplate或者errorMessageArgs为空
     */
    public static void checkArgument(boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

    /**
     * 检查实例的状态,不涉及调用方法的任何参数
     *
     * @param expression 布尔表达式
     * @throws IllegalArgumentException 表达式不成立
     */
    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    /**
     * 检查实例的状态,不涉及调用方法的任何参数
     *
     * @param expression   布尔表达式
     * @param errorMessage 错误提示信息
     * @throws IllegalArgumentException 表达式不成立
     */
    public static void checkState(boolean expression, @Nullable Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    /**
     * 检查实例的状态,不涉及调用方法的任何参数
     *
     * @param expression           表达式合法性
     * @param errorMessageTemplate 错误提示信息模板
     * @param errorMessageArgs     替换的内容
     * @throws IllegalArgumentException 表达式不成立
     * @throws NullPointerException     errorMessageTemplate或者errorMessageArgs为空
     */
    public static void checkState(boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalStateException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

    /**
     * 检查对象的引用是否为空
     *
     * @param reference 对象的引用
     * @return 原对象引用
     * @throws NullPointerException 该引用对象为空
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    /**
     * 检查对象的引用是否为空
     *
     * @param reference    对象的引用
     * @param errorMessage 错误提示信息
     * @return 原对象引用
     * @throws NullPointerException 该引用对象为空
     */
    public static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    /**
     * 检查对象的引用是否为空
     *
     * @param reference            对象的引用
     * @param errorMessageTemplate 错误提示信息模板
     * @param errorMessageArgs     替换的内容
     * @return 原对象引用
     * @throws NullPointerException 该引用对象为空
     */
    public static <T> T checkNotNull(T reference, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
        if (reference == null) {
            // If either of these parameters is null, the right thing happens anyway
            throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }

    /**
     * 检查指定元素在array,list,string中的合法性
     *
     * @param index 索引
     * @param size  长度
     * @return index
     * @throws IndexOutOfBoundsException index不在范围内
     * @throws IllegalArgumentException  size < 0
     */
    public static int checkElementIndex(int index, int size) {
        return checkElementIndex(index, size, "index");
    }

    /**
     * 检查指定元素在array,list,string中的合法性
     *
     * @param index 索引
     * @param size  长度
     * @param desc  异常时,描述该index错误信息的文本
     * @return index
     * @throws IndexOutOfBoundsException index不在范围内
     * @throws IllegalArgumentException  size < 0
     */
    public static int checkElementIndex(int index, int size, @Nullable String desc) {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
        }
        return index;
    }

    private static String badElementIndex(int index, int size, String desc) {
        if (index < 0) {
            return format("%s (%s) must not be negative", desc, index);
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else { // index >= size
            return format("%s (%s) must be less than size (%s)", desc, index, size);
        }
    }

    /**
     * 检查指定position在array,list,string中的合法性
     *
     * @param index 索引
     * @param size  长度
     * @return index
     * @throws IndexOutOfBoundsException index不在范围内
     * @throws IllegalArgumentException  size < 0
     */
    public static int checkPositionIndex(int index, int size) {
        return checkPositionIndex(index, size, "index");
    }

    /**
     * 检查指定position在array,list,string中的合法性
     *
     * @param index 索引
     * @param size  长度
     * @param desc  异常时,描述该index错误信息的文本
     * @return index
     * @throws IndexOutOfBoundsException index不在范围内
     * @throws IllegalArgumentException  size < 0
     */
    public static int checkPositionIndex(int index, int size, @Nullable String desc) {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
        }
        return index;
    }

    private static String badPositionIndex(int index, int size, String desc) {
        if (index < 0) {
            return format("%s (%s) must not be negative", desc, index);
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else { // index > size
            return format("%s (%s) must not be greater than size (%s)", desc, index, size);
        }
    }

    /**
     * 检查指定范围在array,list,string中的合法性
     *
     * @param start 开始位置
     * @param end   结束位置
     * @param size  长度
     * @throws IndexOutOfBoundsException start < 0 || end < start || end > size
     * @throws IllegalArgumentException  size < 0
     */
    public static void checkPositionIndexes(int start, int end, int size) {
        // Carefully optimized for execution by hotspot (explanatory comment above)
        if (start < 0 || end < start || end > size) {
            throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
        }
    }

    private static String badPositionIndexes(int start, int end, int size) {
        if (start < 0 || start > size) {
            return badPositionIndex(start, size, "start index");
        }
        if (end < 0 || end > size) {
            return badPositionIndex(end, size, "end index");
        }
        // end < start
        return format("end index (%s) must not be less than start index (%s)", end, start);
    }

    static String format(@Nullable String template, @Nullable Object... args) {
        template = String.valueOf(template); // null -> "null"

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template.substring(templateStart, placeholderStart));
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template.substring(templateStart));

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }
}
