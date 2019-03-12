package com.zp.course.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see TouchViewGroup
 * @since 2019/3/5
 */
public class TouchViewGroup extends ViewGroup {

    private static final String TAG = TouchViewGroup.class.getSimpleName();

    public TouchViewGroup(Context context) {
        super(context);
    }

    public TouchViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TouchViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 分发点击事件
     *
     * @param ev 点击事件
     * @return true
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "分发按下事件");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "分发滑动事件");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "分发抬起事件");
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 拦截点击事件
     *
     * @param ev 点击事件
     * @return true 拦截改事件   false 反之
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "拦截按下事件");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "拦截滑动事件");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "拦截抬起事件");
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();

        Log.e(TAG, count + "l = " + l + ", t = " + t + ", r = " + r + ", b = " + b);
        if (count == 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);

            view.layout(l, t, l + view.getMeasuredWidth(), t + view.getMeasuredHeight());
        }
    }

    /**
     * 消耗点击事件
     *
     * @param event 点击事件
     * @return true 该事件已消耗 false 反之
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "消耗按下事件");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "消耗滑动事件");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "消耗抬起事件");
                break;
        }
        return super.onTouchEvent(event);
    }
}
