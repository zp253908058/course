package com.zp.course.storage.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.zp.course.util.log.Logger;
import com.zp.course.util.ObjectUtils;
import com.zp.course.util.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;


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

    /**
     * 以{@Context.MODE_PRIVATE}的方式保存
     *
     * @param context Context
     * @param object  所要保存的实体对象
     */
    public boolean save(Context context, Object object) {
        return save(context, object, Context.MODE_PRIVATE);
    }

    /**
     * 保存
     *
     * @param context Context
     * @param object  所要保存的实体对象
     * @param mode    保存的方式
     */
    public boolean save(Context context, Object object, int mode) {
        SharedPreferences preferences = getSharedPreferences(context, object, mode);
        try {
            putInSharedPreferences(object, preferences);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    private SharedPreferences getSharedPreferences(Context context, Object object, int mode) {
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
        if (Validator.isEmpty(name)) {
            throw new IllegalArgumentException("@SharedPreference注解name属性不能为空。");
        }

        return context.getSharedPreferences(name, mode);
    }

    private void putInSharedPreferences(Object object, SharedPreferences sp) throws IllegalAccessException {
        List<Field> fields = ObjectUtils.getAllDeclaredFields(object);

        SharedPreferences.Editor editor = sp.edit();
        for (Field field : fields) {
            if (field.isSynthetic()) {
                continue;
            }
            Annotation annotation = field.getAnnotation(Ignore.class);
            if (annotation != null) {
                continue;
            }
            field.setAccessible(true);
            String type = field.getType().getSimpleName();
            String key = field.getName();

            putValueInSharedPreferences(key, type, field.get(object), editor);
        }
        editor.apply();
    }

    private void putValueInSharedPreferences(String key, String type, Object value, SharedPreferences.Editor editor) {
        switch (type) {
            case "int":
                editor.putInt(key, (int) value);
                break;
            case "float":
                editor.putFloat(key, (float) value);
                break;
            case "long":
                editor.putLong(key, (long) value);
                break;
            case "boolean":
                editor.putBoolean(key, (boolean) value);
                break;
            case "String":
                editor.putString(key, (String) value);
                break;
            case "Set":
                //noinspection unchecked
                editor.putStringSet(key, (Set<String>) value);
                break;
            case "byte":
            case "short":
            case "double":
            case "char":
            default:
                Logger.e("unsupported type : " + type);
                break;
        }
    }

    public boolean load(Context context, Object object) {
        return load(context, object, Context.MODE_PRIVATE);
    }

    public boolean load(Context context, Object object, int mode) {
        SharedPreferences preferences = getSharedPreferences(context, object, mode);
        try {
            loadInSharedPreferences(object, preferences);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void loadInSharedPreferences(Object object, SharedPreferences sp) throws IllegalAccessException {
        List<Field> fields = ObjectUtils.getAllDeclaredFields(object);

        for (Field field : fields) {
            if (field.isSynthetic()) {
                continue;
            }
            Annotation annotation = field.getAnnotation(Ignore.class);
            if (annotation != null) {
                continue;
            }
            field.setAccessible(true);
            loadValueInSharedPreferences(object, field, sp);
        }
    }

    private void loadValueInSharedPreferences(Object object, Field field, SharedPreferences preferences) throws IllegalAccessException {
        String type = field.getType().getSimpleName();
        String key = field.getName();
        switch (type) {
            case "int":
                field.set(object, preferences.getInt(key, 0));
                break;
            case "float":
                field.set(object, preferences.getFloat(key, 0));
                break;
            case "long":
                field.set(object, preferences.getLong(key, 0));
                break;
            case "boolean":
                field.set(object, preferences.getBoolean(key, false));
                break;
            case "String":
                field.set(object, preferences.getString(key, ""));
                break;
            case "Set":
                //noinspection unchecked
                field.set(object, preferences.getStringSet(key, null));
                break;
            case "byte":
            case "short":
            case "double":
            case "char":
            default:
                Logger.e("unsupported type : " + type);
                break;
        }
    }
}