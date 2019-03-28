package com.zp.course.ui.timetable;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zp.course.R;
import com.zp.course.app.ToolbarActivity;
import com.zp.course.pop.DialogFactory;
import com.zp.course.storage.database.AppDatabase;
import com.zp.course.storage.database.dao.TimetableDao;
import com.zp.course.storage.database.table.ClassEntity;
import com.zp.course.storage.database.table.TimetableEntity;
import com.zp.course.util.Validator;
import com.zp.course.widget.WheelView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see TimetableAddActivity
 * @since 2019/3/20
 */
public class TimetableAddActivity extends ToolbarActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnClickListener {

    private static final String DATE_FORMAT = "%1$d-%2$d-%3$d";

    public static void go(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TimetableAddActivity.class);
        context.startActivity(intent);
    }

    private EditText mNameText;
    private TextView mDurationText;
    private TextView mWeekText;
    private TextView mDateText;
    private ClassAdapter mAdapter;

    private AlertDialog mDurationDialog;
    private AlertDialog mWeekDialog;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private AlertDialog mPromitDialog;
    private long mStartMills;

    private TimetableEntity mEntity = new TimetableEntity();
    private TimetableDao mDao = AppDatabase.getInstance().getTimetableDao();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_add_layout);
        showFloatingButton(true);
        setFloatingIcon(getDrawable(R.drawable.ic_add_black));

        initView();
    }

    private void initView() {
        mNameText = findViewById(R.id.timetable_add_name);
        mDurationText = findViewById(R.id.timetable_add_duration);
        mWeekText = findViewById(R.id.timetable_add_week_count);
        mDateText = findViewById(R.id.timetable_add_date);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ClassAdapter(null);
        recyclerView.setAdapter(mAdapter);

        List<String> duration = Arrays.asList(getResources().getStringArray(R.array.duration_select));
        mDurationDialog = DialogFactory.createOptionMenuDialog(this, duration, (parent, view, position, id) -> {
            String item = (String) parent.getAdapter().getItem(position);
            mDurationText.setText(item);
            mDurationDialog.dismiss();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle_NoTitle);
        View view = getLayoutInflater().inflate(R.layout.common_wheel_view, null);
        WheelView wheelView = view.findViewById(R.id.wheel_view);
        wheelView.setDataList(Arrays.asList(getResources().getStringArray(R.array.week_count_select)));
        wheelView.setOnWheelChangeListener((item, position) -> mWeekText.setText(item));
        wheelView.setMaximumWidthText("00");
        builder.setPositiveButton(getString(R.string.text_ok), (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton(getString(R.string.text_cancel), (dialog, which) -> {
            mWeekText.setText("");
            dialog.dismiss();
        });
        builder.setView(view);
        mWeekDialog = builder.create();

        mDatePickerDialog = DialogFactory.createDatePickerDialog(this, this);
        mTimePickerDialog = DialogFactory.createTimePickerDialog(this, this);
        mPromitDialog = DialogFactory.createAlertDialog(this, "本期课表已存在，点击确定将对课表进行覆盖操作，是否继续？", this);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.timetable_add_duration_layout:
                showDurationDialog();
                break;
            case R.id.timetable_add_week_count_layout:
                showWeekSelectDialog();
                break;
            case R.id.timetable_add_date_layout:
                showDatePickerDialog();
                break;
        }

    }

    private void showDurationDialog() {
        mDurationDialog.show();
    }

    private void showWeekSelectDialog() {
        mWeekDialog.show();
    }

    private void showDatePickerDialog() {
        mDatePickerDialog.show();
    }

    @Override
    public void onFloatingClick() {
        mTimePickerDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timetable_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        attemptAdd();
        return true;
    }

    private void attemptAdd() {
        String name = mNameText.getText().toString();
        if (Validator.isEmpty(name)) {
            mNameText.setError(getString(R.string.error_timetable_name_empty));
            mNameText.requestFocus();
            return;
        }

        String duration = mDurationText.getText().toString();
        if (Validator.isEmpty(duration)) {
            mDurationText.setError(getString(R.string.error_timetable_duration_empty));
            return;
        }

        String week = mWeekText.getText().toString();
        if (Validator.isEmpty(week)) {
            mWeekText.setError(getString(R.string.error_timetable_week_empty));
            return;
        }

        String startDate = mDateText.getText().toString();
        if (Validator.isEmpty(startDate)) {
            mDateText.setError(getString(R.string.error_timetable_start_date_empty));
            return;
        }

        mEntity.setName(name);
        mEntity.setDuration(Integer.parseInt(duration));
        mEntity.setWeekCount(Integer.parseInt(week));
        mEntity.setStartMills(mStartMills);
        mEntity.setUpdateTime(System.currentTimeMillis());

        long id = mDao.findByWeek(Integer.parseInt(week));
        boolean check = id > 0;
        if (check) {
            mEntity.setId(id);
            mPromitDialog.show();
            return;
        }
        mDao.add(mEntity);
        mEntity = mDao.getLastOne();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = String.format(Locale.getDefault(), DATE_FORMAT, year, month + 1, dayOfMonth);
        mDateText.setText(date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        mStartMills = calendar.getTimeInMillis();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mDao.update(mEntity);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        ClassEntity entity = new ClassEntity();
        entity.setSection(mAdapter.getItemCount() + 1);
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, hourOfDay, minute);
        entity.setStartTime(calendar.getTimeInMillis());

    }
}

