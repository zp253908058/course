package com.zp.course.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.core.util.Preconditions;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see Toaster
 * @since 2019/3/11
 */
public class Toaster {
    private Toaster() {
        throw new UnsupportedOperationException("Can not invoke Constructor!");
    }

    /**
     * 显示文本的Toast
     */
    private static volatile Toast sToast;

    /**
     * 初始化工具类
     *
     * @param context application的Context，Toast的上下文与Activity无关
     */
    public static void initialize(Context context) {
        if (sToast == null) {
            synchronized (Toaster.class) {
                if (sToast == null) {
                    sToast = createToast(context);
                }
            }
        }
    }

    private static Toast createToast(Context context) {
        return Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    /**
     * 取消Toast显示
     */
    public static void cancel() {
        Preconditions.checkNotNull(sToast, "Toaster must initialize before use.");
        sToast.cancel();
    }

    /**
     * 显示提示消息
     *
     * @param message  提示文本
     * @param duration 提示时长
     * @param margins  margins
     * @param gravity  显示位置
     */
    public static void showToast(CharSequence message, int duration, Margins margins, Gravity gravity) {
        Preconditions.checkNotNull(sToast, "Toaster must initialize before use.");
        sToast.setText(message);
        sToast.setDuration(duration);
        if (margins != null) {
            sToast.setMargin(margins.horizontalMargin, margins.verticalMargin);
        }
        if (gravity != null) {
            sToast.setGravity(gravity.gravity, gravity.xOffset, gravity.yOffset);
        }
        sToast.show();
    }

    /**
     * 显示提示消息
     *
     * @param message  提示文本
     * @param duration 提示时长
     */
    public static void showToast(CharSequence message, int duration) {
        Preconditions.checkNotNull(sToast, "Toaster must initialize before use.");
        sToast.setText(message);
        sToast.setDuration(duration);
        sToast.show();
    }

    /**
     * 显示提示消息
     *
     * @param message 提示文本
     */
    public static void showToast(CharSequence message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    /**
     * 显示提示消息
     *
     * @param resId    提示文本资源id
     * @param duration 提示时长
     * @param margins  margins
     * @param gravity  显示位置
     */
    public static void showToast(@StringRes int resId, int duration, Margins margins, Gravity gravity) {
        Context context = sToast.getView().getContext().getApplicationContext();
        showToast(context.getResources().getText(resId), duration, margins, gravity);
    }

    /**
     * 显示提示消息
     *
     * @param resId    提示文本资源id
     * @param duration 提示时长
     */
    public static void showToast(@StringRes int resId, int duration) {
        Context context = sToast.getView().getContext().getApplicationContext();
        showToast(context.getResources().getText(resId), duration);
    }

    /**
     * 显示提示消息
     *
     * @param resId 提示文本资源id
     */
    public static void showToast(@StringRes int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public static class Margins {
        public float horizontalMargin;
        public float verticalMargin;
    }

    public static class Gravity {
        public int gravity;
        public int xOffset;
        public int yOffset;
    }
}
