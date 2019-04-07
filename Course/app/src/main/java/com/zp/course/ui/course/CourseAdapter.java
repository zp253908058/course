package com.zp.course.ui.course;

import com.zp.course.R;
import com.zp.course.app.RecyclerAdapter;
import com.zp.course.app.RecyclerViewHolder;
import com.zp.course.storage.database.table.CourseInfoEntity;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Class description:
 *
 * @author ZP
 * @version 1.0
 * @see CourseAdapter
 * @since 2019/4/2
 */
public class CourseAdapter extends RecyclerAdapter<CourseInfoEntity> {

    /**
     * the constructor of this class.
     *
     * @param items the data source.
     */
    public CourseAdapter(@Nullable List<CourseInfoEntity> items) {
        super(items);
    }

    @Override
    protected int getLayoutByViewType(int viewType) {
        return R.layout.item_course_info_layout;
    }

    @Override
    protected void onBindViewHolder(RecyclerViewHolder holder, int position, CourseInfoEntity item) {

    }
}
