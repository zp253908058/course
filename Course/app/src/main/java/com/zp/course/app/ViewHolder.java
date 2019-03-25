package com.zp.course.app;

import android.view.View;

import com.zp.course.util.Validator;

import androidx.annotation.IdRes;
import androidx.collection.SparseArrayCompat;

/**
 * Class description:
 *
 * @author ZP
 * @version 1.0
 * @see ViewHolder
 * @since 2019/3/25
 */
public class ViewHolder {
    private static final String TAG = ViewHolder.class.getSimpleName();
    private SparseArrayCompat<View> mHolder = null;
    private View mItemView;

    public ViewHolder() {
        this(null);
    }

    public ViewHolder(View itemView) {
        this.mItemView = itemView;
        mHolder = new SparseArrayCompat<>();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T get(@IdRes int resId) {
        View view = mHolder.get(resId);
        if (Validator.isNull(view)) {
            view = mItemView.findViewById(resId);
            mHolder.put(resId, view);
        }
        return (T) view;
    }

    public View getItemView() {
        return mItemView;
    }

    public void setItemView(View itemView) {
        this.mItemView = itemView;
    }

    public void clear() {
        mItemView = null;
        mHolder.clear();
    }
}
