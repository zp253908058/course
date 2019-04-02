package com.zp.course.ui.course;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zp.course.R;
import com.zp.course.app.RecyclerViewTouchListener;
import com.zp.course.app.ToolbarActivity;
import com.zp.course.app.UserManager;
import com.zp.course.pop.DialogFactory;
import com.zp.course.storage.database.AppDatabase;
import com.zp.course.storage.database.dao.CourseDao;
import com.zp.course.storage.database.dao.TimetableDao;
import com.zp.course.storage.database.table.ClassEntity;
import com.zp.course.storage.database.table.CourseEntity;
import com.zp.course.storage.database.table.CourseInfoEntity;
import com.zp.course.storage.database.table.TimetableEntity;
import com.zp.course.ui.timetable.ClassAdapter;
import com.zp.course.ui.timetable.TimetableAddOrUpdateActivity;
import com.zp.course.util.DateTimeUtils;
import com.zp.course.util.Toaster;
import com.zp.course.util.Validator;

import java.util.ArrayList;
import java.util.List;

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
 * @see CourseAddOrUpdateActivity
 * @since 2019/3/15
 */
public class CourseAddOrUpdateActivity extends ToolbarActivity implements RecyclerViewTouchListener.OnItemClickListener, RecyclerViewTouchListener.OnItemLongClickListener {

    private static final int REQUEST_CODE = 0x1 << 1;
    private static final int INVALID_POSITION = -1;

    public static void go(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CourseAddOrUpdateActivity.class);
        context.startActivity(intent);
    }

    private EditText mNameText;
    private EditText mNumberText;
    private TextView mTimetableText;
    private EditText mDescriptionText;
    private CourseDao mCourseDao;
    private CourseAdapter mAdapter;

    private AlertDialog mTimetableDialog;
    private AlertDialog mDeleteDialog;
    private long mTimetableId = 0;
    private int mDeletePosition = INVALID_POSITION;
    private String mDeleteTipString;
    private long mUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add_layout);
        showFloatingButton(true);
        setFloatingIcon(getDrawable(R.drawable.ic_add_black));
        initView();
    }

    private void initView() {
        mNameText = findViewById(R.id.course_add_name);
        mNumberText = findViewById(R.id.course_add_number);
        mTimetableText = findViewById(R.id.course_add_timetable);
        mDescriptionText = findViewById(R.id.course_add_description);

        mCourseDao = AppDatabase.getInstance().getCourseDao();

        TimetableDao timetableDao = AppDatabase.getInstance().getTimetableDao();
        mUserId = UserManager.getInstance().getUser().getId();
        List<TimetableEntity> timetables = timetableDao.getAll(mUserId);
        AlertDialog promptDialog = DialogFactory.createAlertDialog(this, getString(R.string.tip_timetable_null), (dialog, which) -> {
            TimetableAddOrUpdateActivity.go(CourseAddOrUpdateActivity.this);
            finish();
        }, (dialog, which) -> finish());
        if (Validator.isEmpty(timetables)) {
            promptDialog.show();
            return;
        }
        mTimetableDialog = DialogFactory.createOptionMenuDialog(this, timetables, (parent, view, position, id) -> {
            TimetableEntity item = (TimetableEntity) parent.getAdapter().getItem(position);
            mTimetableText.setText(item.toString());
            mTimetableId = item.getId();
            mTimetableDialog.dismiss();
        });


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, this, this));
        mAdapter = new CourseAdapter(new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        mDeleteTipString = getString(R.string.tip_course_add_delete);
        mDeleteDialog = DialogFactory.createAlertDialog(this, "", (dialog, which) -> {
            CourseInfoEntity entity = mAdapter.getItem(mDeletePosition);
            mCourseDao.deleteCourseInfo(entity);
            mAdapter.remove(mDeletePosition);
            mDeletePosition = INVALID_POSITION;
            dialog.dismiss();
        });
    }

    @Override
    public void onFloatingClick() {
        CourseInfoAddOrUpdateActivity.goForResult(this, REQUEST_CODE);
    }

    public void onClick(View view) {
        mTimetableDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timetable_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        attemptSave();
        return true;
    }

    private void attemptSave() {
        String name = mNameText.getText().toString();
        if (Validator.isEmpty(name)) {
            mNameText.setError(getString(R.string.error_course_name_empty));
            mNameText.requestFocus();
            return;
        }

        String number = mNumberText.getText().toString();
        if (Validator.isEmpty(number)) {
            mNumberText.setError(getString(R.string.error_course_number_empty));
            mNumberText.requestFocus();
            return;
        }

        String description = mDescriptionText.getText().toString();

        boolean success = verify(name, number);

        if (success) {
            CourseEntity entity = new CourseEntity();
            entity.setName(name);
            entity.setNumber(number);
            entity.setDescription(description);
            entity.setUserId(UserManager.getInstance().getUser().getId());
            save(entity);
        } else {
            Toaster.showToast(R.string.tip_login_failed);
        }
    }

    private boolean verify(String name, String number) {
        return mCourseDao.verify(name, number) == 0;
    }

    private void save(CourseEntity entity) {
        mCourseDao.save(entity);
    }

    @Override
    public void onItemClick(View view, int position) {
        CourseInfoAddOrUpdateActivity.goForResult(this, mAdapter.getItem(position).getId(), REQUEST_CODE);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        mDeletePosition = position;
        CourseInfoEntity entity = mAdapter.getItem(position);
        String deleteString = String.format(mDeleteTipString, entity.getDayInWeek(), entity.getDescription());
        mDeleteDialog.setMessage(deleteString);
        mDeleteDialog.show();
    }
}
