package com.zp.course.ui.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.zp.course.R;
import com.zp.course.app.ToolbarActivity;
import com.zp.course.pop.DialogFactory;
import com.zp.course.storage.database.AppDatabase;
import com.zp.course.storage.database.dao.TimetableDao;
import com.zp.course.storage.database.table.ClassEntity;
import com.zp.course.storage.database.table.CourseInfoEntity;
import com.zp.course.util.Toaster;
import com.zp.course.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

/**
 * Class description:
 *
 * @author ZP
 * @version 1.0
 * @see CourseInfoAddOrUpdateActivity
 * @since 2019/4/2
 */
public class CourseInfoAddOrUpdateActivity extends ToolbarActivity {

    private static final String KEY_TIMETABLE_ID = "timetable_id";
    public static final String KEY_ENTITY = "entity";

    public static void goForResult(Activity context, long timetableId, int requestCode) {
        goForResult(context, timetableId, null, requestCode);
    }

    public static void goForResult(Activity context, long timetableId, CourseInfoEntity entity, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, CourseInfoAddOrUpdateActivity.class);
        if (entity != null) {
            intent.putExtra(KEY_ENTITY, entity);
        }
        intent.putExtra(KEY_TIMETABLE_ID, timetableId);
        context.startActivityForResult(intent, requestCode);
    }

    private EditText mNameText;
    private EditText mAddressText;
    private TextView mWeekView;
    private TextView mSectionView;
    private EditText mDescriptionView;

    private long mTimetableId = 0;

    private AlertDialog mWeekDialog;
    private AlertDialog mSectionDialog;
    private int mWeekSelectPosition;

    private CourseInfoEntity mEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info_add_or_update_layout);

        initView();
    }

    private void initView() {
        mNameText = findViewById(R.id.course_add_teacher_name);
        mAddressText = findViewById(R.id.course_add_address);
        mWeekView = findViewById(R.id.course_add_day_in_week);
        mSectionView = findViewById(R.id.course_add_section);
        mDescriptionView = findViewById(R.id.course_add_description);

        List<String> weeks = Arrays.asList(getResources().getStringArray(R.array.week));
        mWeekDialog = DialogFactory.createOptionMenuDialog(this, weeks, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mWeekSelectPosition = position;
                String week = (String) parent.getAdapter().getItem(position);
                mWeekView.setText(week);
                mWeekDialog.dismiss();
            }
        });

        Intent intent = getIntent();
        mTimetableId = intent.getLongExtra(KEY_TIMETABLE_ID, 0);

        mSectionDialog = DialogFactory.createWheelDialog(this, getSections(), (item, position) -> mSectionView.setText(item), (dialog, which) -> dialog.dismiss(), (dialog, which) -> {
            mSectionView.setText("");
            dialog.dismiss();
        });

        if (intent.hasExtra(KEY_ENTITY)) {
            mEntity = intent.getParcelableExtra(KEY_ENTITY);

        } else {
            mEntity = new CourseInfoEntity();
            setTitle(R.string.label_course_info_update);
        }
    }

    private List<String> getSections() {
        TimetableDao dao = AppDatabase.getInstance().getTimetableDao();
        List<ClassEntity> classes = dao.getClassList(mTimetableId);
        List<String> result = new ArrayList<>();
        if (Validator.isEmpty(classes)) {
            return result;
        }
        for (ClassEntity entity : classes) {
            result.add(String.valueOf(entity.getSection()));
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        attemptDone();
        return true;
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.course_add_day_in_week_layout:
                showWeekDialog();
                break;
            case R.id.course_add_section_layout:
                showSectionDialog();
                break;
        }
    }

    private void showWeekDialog() {
        mWeekDialog.show();
    }

    private void showSectionDialog() {
        mSectionDialog.show();
    }

    private void attemptDone() {
        String name = mNameText.getText().toString();
        String address = mAddressText.getText().toString();
        if (Validator.isEmpty(address)) {
            mAddressText.setError(getString(R.string.error_course_address_null));
            mAddressText.requestFocus();
            return;
        }
        String week = mWeekView.getText().toString();
        if (Validator.isEmpty(week)) {
            Toaster.showToast(R.string.prompt_select_week);
            return;
        }
        String section = mSectionView.getText().toString();
        if (Validator.isEmpty(address)) {
            Toaster.showToast(R.string.prompt_select_section);
            return;
        }
        String description = mDescriptionView.getText().toString();
        mEntity.setTeacherName(name);
        mEntity.setAddress(address);
        mEntity.setDayInWeek(mWeekSelectPosition + 1);
        mEntity.setSection(Integer.parseInt(section));
        mEntity.setDescription(description);

        Intent intent = new Intent();
        intent.putExtra(KEY_ENTITY, mEntity);
        setResult(RESULT_OK, intent);
        finish();
    }
}
