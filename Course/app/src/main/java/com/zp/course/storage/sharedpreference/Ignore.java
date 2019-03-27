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
 * @see Ignore
 * @since 2019/3/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Ignore {

}
