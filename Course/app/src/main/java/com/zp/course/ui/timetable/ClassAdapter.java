package com.zp.course.ui.timetable;

import android.widget.TextView;

import com.zp.course.R;
import com.zp.course.app.RecyclerAdapter;
import com.zp.course.app.RecyclerViewHolder;
import com.zp.course.storage.database.table.ClassEntity;
import com.zp.course.util.DateTimeUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see ClassAdapter
 * @since 2019/3/28
 */
public class ClassAdapter extends RecyclerAdapter<ClassEntity> {

    Calendar mCalendar = Calendar.getInstance();

    /**
     * the constructor of this class.
     *
     * @param items the data source.
     */
    public ClassAdapter(@Nullable List<ClassEntity> items) {
        super(items);
    }

    @Override
    protected int getLayoutByViewType(int viewType) {
        return R.layout.item_class_layout;
    }

    @Override
    protected void onBindViewHolder(RecyclerViewHolder holder, int position, ClassEntity item) {
        TextView section = holder.get(R.id.class_section);
        TextView duration = holder.get(R.id.class_duration);
        TextView time = holder.get(R.id.class_time);

        if (item != null) {
            section.setText(String.valueOf(item.getSection()));
            time.setText(DateTimeUtils.getTime(item.getStartTime()));
            mCalendar.setTime(new Date(item.getStartTime()));
            duration.setText(mCalendar.get(Calendar.AM_PM) == Calendar.AM ? "上午" : "下午");
        }
    }
}
