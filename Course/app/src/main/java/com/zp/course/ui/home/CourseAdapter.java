package com.zp.course.ui.home;

import android.widget.TextView;

import com.zp.course.R;
import com.zp.course.app.RecyclerAdapter;
import com.zp.course.app.RecyclerViewHolder;
import com.zp.course.storage.database.table.CourseEntity;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see CourseAdapter
 * @since 2019/3/14
 */
public class CourseAdapter extends RecyclerAdapter<CourseEntity> {
    /**
     * the constructor of this class.
     *
     * @param items the data source.
     */
    public CourseAdapter(@Nullable List<CourseEntity> items) {
        super(items);
    }

    @Override
    protected int getLayoutByViewType(int viewType) {
        return R.layout.item_course_layout;
    }

    @Override
    protected void onBindViewHolder(RecyclerViewHolder holder, int position, CourseEntity item) {
        TextView text = holder.get(R.id.course_name);

        if (item != null) {
            text.setText(item.getName());
        }
    }
}
