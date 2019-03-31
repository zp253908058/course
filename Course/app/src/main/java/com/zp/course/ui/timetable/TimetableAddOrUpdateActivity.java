package com.zp.course.ui.timetable;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import com.zp.course.app.RecyclerViewTouchListener;
import com.zp.course.app.ToolbarActivity;
import com.zp.course.app.UserManager;
import com.zp.course.model.TimetableClassEntity;
import com.zp.course.pop.DialogFactory;
import com.zp.course.storage.database.AppDatabase;
import com.zp.course.storage.database.dao.TimetableDao;
import com.zp.course.storage.database.table.ClassEntity;
import com.zp.course.storage.database.table.TimetableEntity;
import com.zp.course.util.DateTimeUtils;
import com.zp.course.util.Toaster;
import com.zp.course.util.Validator;

import java.util.ArrayList;
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
 * @see TimetableAddOrUpdateActivity
 * @since 2019/3/20
 */
public class TimetableAddOrUpdateActivity extends ToolbarActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, RecyclerViewTouchListener.OnItemClickListener, RecyclerViewTouchListener.OnItemLongClickListener {

    private static final String KEY_ID = "id";
    private static final String DATE_FORMAT = "%1$d-%2$d-%3$d";
    private static final int INVALID_POSITION = -1;

    public static void go(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TimetableAddOrUpdateActivity.class);
        context.startActivity(intent);
    }

    public static void goForResult(Activity context, int requestCode) {
        goForResult(context, 0, requestCode);
    }

    public static void goForResult(Activity context, long id, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, TimetableAddOrUpdateActivity.class);
        if (id > 0) {
            intent.putExtra(KEY_ID, id);
        }
        context.startActivityForResult(intent, requestCode);
    }

    private long mTimetableId = 0;

    private EditText mNameText;
    private TextView mDurationText;
    private TextView mTermText;
    private TextView mWeekCountText;
    private TextView mDateText;
    private ClassAdapter mAdapter;
    private int mClickPosition = INVALID_POSITION;
    private int mDeletePosition = INVALID_POSITION;
    private String mDeleteTipString;

    private AlertDialog mDurationDialog;
    private AlertDialog mTermDialog;
    private AlertDialog mWeekCountDialog;
    private AlertDialog mDeleteDialog;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private long mStartMills;

    private TimetableClassEntity mEntity;
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

        //初始化控件
        mNameText = findViewById(R.id.timetable_add_name);
        mDurationText = findViewById(R.id.timetable_add_duration);
        mTermText = findViewById(R.id.timetable_add_term);
        mWeekCountText = findViewById(R.id.timetable_add_week_count);
        mDateText = findViewById(R.id.timetable_add_date);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, this, this));
        mAdapter = new ClassAdapter(new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        //初始化Id，如果有id说明是更新，没有id说明是新增
        Intent intent = getIntent();
        if (intent.hasExtra(KEY_ID)) {
            mTimetableId = intent.getLongExtra(KEY_ID, 0);
        }

        if (mTimetableId > 0) {
            mEntity = mDao.findById(mTimetableId);
            mNameText.setText(mEntity.getTimetable().getName());
            mDurationText.setText(String.valueOf(mEntity.getTimetable().getDuration()));
            mTermText.setText(String.valueOf(mEntity.getTimetable().getTerm()));
            mWeekCountText.setText(String.valueOf(mEntity.getTimetable().getWeekCount()));
            mStartMills = mEntity.getTimetable().getStartMills();
            mDateText.setText(DateTimeUtils.getDate(mStartMills));
            mAdapter.setItems(mEntity.getClassInfo());
            setTitle(R.string.label_timetable_update);
        } else {
            mEntity = new TimetableClassEntity();
        }

        mDeleteTipString = getString(R.string.tip_timetable_add_delete_class);

        List<String> duration = Arrays.asList(getResources().getStringArray(R.array.duration_select));
        mDurationDialog = DialogFactory.createOptionMenuDialog(this, duration, (parent, view, position, id) -> {
            String item = (String) parent.getAdapter().getItem(position);
            mDurationText.setText(item);
            mDurationDialog.dismiss();
        });

        mTermDialog = DialogFactory.createWheelDialog(this, R.array.term_select, (item, position) -> mTermText.setText(item), (dialog, which) -> {
            int term = Integer.parseInt(mTermText.getText().toString());
            long id = mDao.findByTerm(term);
            if (id > 0) {
                Toaster.showToast(R.string.tip_timetable_add_term_already_exist);
                mTermText.setText("");
            }
            dialog.dismiss();
        }, (dialog, which) -> {
            mTermText.setText("");
            dialog.dismiss();
        });

        mWeekCountDialog = DialogFactory.createWheelDialog(this, R.array.week_count_select, 20, "00", (item, position) -> mWeekCountText.setText(item), (dialog, which) -> dialog.dismiss(), (dialog, which) -> {
            mTermText.setText("");
            dialog.dismiss();
        });
        mDeleteDialog = DialogFactory.createAlertDialog(this, "", (dialog, which) -> {
            ClassEntity entity = mAdapter.getItem(mDeletePosition);
            mDao.deleteClass(entity);
            mAdapter.remove(mDeletePosition);
            mDeletePosition = INVALID_POSITION;
            dialog.dismiss();
        });

        mDatePickerDialog = DialogFactory.createDatePickerDialog(this, this);

        mTimePickerDialog = DialogFactory.createTimePickerDialog(this, this);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.timetable_add_duration_layout:
                showDurationDialog();
                break;
            case R.id.timetable_add_term_layout:
                showTermSelectDialog();
                break;
            case R.id.timetable_add_week_count_layout:
                showWeekCountSelectDialog();
                break;
            case R.id.timetable_add_date_layout:
                showDatePickerDialog();
                break;
        }

    }

    private void showDurationDialog() {
        mDurationDialog.show();
    }

    private void showTermSelectDialog() {
        mTermDialog.show();
    }

    private void showWeekCountSelectDialog() {
        mWeekCountDialog.show();
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

        String term = mTermText.getText().toString();
        if (Validator.isEmpty(term)) {
            mTermText.setError(getString(R.string.error_timetable_term_empty));
            return;
        }

        String weekCount = mWeekCountText.getText().toString();
        if (Validator.isEmpty(weekCount)) {
            mTermText.setError(getString(R.string.error_timetable_week_empty));
            return;
        }

        String startDate = mDateText.getText().toString();
        if (Validator.isEmpty(startDate)) {
            mDateText.setError(getString(R.string.error_timetable_start_date_empty));
            return;
        }

        if (mAdapter.getItemCount() == 0) {
            Toaster.showToast(R.string.tip_timetable_add_class_null);
            return;
        }

        TimetableEntity entity = mEntity.getTimetable();
        if (entity == null) {
            entity = new TimetableEntity();
            entity.setUserId(UserManager.getInstance().getUser().getId());
            mEntity.setTimetable(entity);
        }
        entity.setName(name);
        entity.setDuration(Integer.parseInt(duration));
        entity.setTerm(Integer.parseInt(term));
        entity.setWeekCount(Integer.parseInt(weekCount));
        entity.setStartMills(mStartMills);
        entity.setUpdateTime(System.currentTimeMillis());
        mEntity.setClassInfo(mAdapter.getItems());

        if (mTimetableId > 0) {
            mDao.update(entity);
        } else {
            mDao.add(entity);
            entity = mDao.getLastOne();
            mTimetableId = entity.getId();
            mEntity.setTimetable(entity);
        }
        mDao.insertClass(setClassId(mEntity.getClassInfo()));
        setResult(RESULT_OK);
        finish();
    }

    private List<ClassEntity> setClassId(List<ClassEntity> list) {
        for (ClassEntity entity : list) {
            entity.setTimetableId(mTimetableId);
        }
        return list;
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
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, hourOfDay, minute);
        ClassEntity entity;
        if (mClickPosition != INVALID_POSITION) {
            entity = mAdapter.getItem(mClickPosition);
            entity.setStartTime(calendar.getTimeInMillis());
            mAdapter.update(entity, mClickPosition);
            mClickPosition = INVALID_POSITION;
            return;
        }
        entity = new ClassEntity();
        entity.setSection(mAdapter.getItemCount() + 1);
        entity.setStartTime(calendar.getTimeInMillis());
        entity.setTimetableId(mTimetableId);
        mAdapter.append(entity);
    }

    @Override
    public void onItemClick(View view, int position) {
        mClickPosition = position;
        ClassEntity entity = mAdapter.getItem(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(entity.getStartTime());
        mTimePickerDialog.updateTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        mTimePickerDialog.show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        mDeletePosition = position;
        ClassEntity entity = mAdapter.getItem(position);
        String deleteString = String.format(mDeleteTipString, DateTimeUtils.getTime(entity.getStartTime()));
        mDeleteDialog.setMessage(deleteString);
        mDeleteDialog.show();
    }
}

