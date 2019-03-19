package com.zp.course.app;

import android.view.View;

import com.zp.course.util.Validator;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see RecyclerViewHolder
 * @since 2019/3/14
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArrayCompat<View> mHolder;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        mHolder = new SparseArrayCompat<>();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T get(@IdRes int resId) {
        View view = mHolder.get(resId);
        if (Validator.isNull(view)) {
            view = itemView.findViewById(resId);
            mHolder.put(resId, view);
        }
        return (T) view;
    }
}
