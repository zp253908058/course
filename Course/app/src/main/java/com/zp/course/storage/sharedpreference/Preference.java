package com.zp.course.storage.sharedpreference;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see Preference
 * @since 2019/3/11
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Preference {

    /**
     * Preference 文件的名字
     *
     * @return String
     */
    String name() default "";
}
