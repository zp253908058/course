package com.zp.course.ui.timetable;

import android.widget.TextView;

import com.zp.course.R;
import com.zp.course.app.RecyclerAdapter;
import com.zp.course.app.RecyclerViewHolder;
import com.zp.course.storage.database.table.TimetableEntity;
import com.zp.course.util.DateTimeUtils;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see TimetableAdapter
 * @since 2019/3/20
 */
public class TimetableAdapter extends RecyclerAdapter<TimetableEntity> {
    /**
     * the constructor of this class.
     *
     * @param items the data source.
     */
    public TimetableAdapter(@Nullable List<TimetableEntity> items) {
        super(items);
    }

    @Override
    protected int getLayoutByViewType(int viewType) {
        return R.layout.item_timetable_layout;
    }

    @Override
    protected void onBindViewHolder(RecyclerViewHolder holder, int position, TimetableEntity item) {
        TextView name = holder.get(R.id.timetable_name);
        TextView term = holder.get(R.id.timetable_term);
        TextView duration = holder.get(R.id.timetable_duration);
        TextView startDate = holder.get(R.id.timetable_start_date);

        if (item != null) {
            name.setText(item.getName());
            term.setText(String.valueOf(item.getTerm()));
            duration.setText(String.valueOf(item.getDuration()));
            startDate.setText(DateTimeUtils.getDate(item.getStartMills()));
        }
    }
}
