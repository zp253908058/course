package com.zp.course.storage.sharedprefence;

import android.content.Context;
import android.content.SharedPreferences;

import com.zp.course.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see SharedPreferenceManager
 * @since 2019/3/11
 */
public class SharedPreferenceManager {
    private static SharedPreferenceManager sInstance;

    private SharedPreferenceManager() {
    }

    public static SharedPreferenceManager getInstance() {
        if (sInstance == null) {
            synchronized (SharedPreferenceManager.class) {
                if (sInstance == null) {
                    sInstance = new SharedPreferenceManager();
                }
            }
        }

        return sInstance;
    }


    public void save(Context context, Object object) {
        Annotation[] annotations = object.getClass().getAnnotations();
        Preference preference = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof Preference) {
                preference = (Preference) annotation;
                break;
            }
        }
        if (preference == null) {
            throw new IllegalArgumentException(object.getClass().getSimpleName() + "类必须使用@SharedPreference注解。");
        }

        String name = preference.name();
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("@SharedPreference注解name属性不能为空。");
        }

        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        Field[] fields = object.getClass().getDeclaredFields();
        SharedPreferences.Editor editor = preferences.edit();
        for (Field field : fields) {
            innerSave(editor, field);
        }
    }

    private void innerSave(SharedPreferences.Editor editor, Field field) {
//        field.
    }
}