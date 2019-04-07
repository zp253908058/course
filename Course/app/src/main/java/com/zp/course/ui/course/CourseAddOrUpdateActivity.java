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
import com.zp.course.model.CourseInfoContractEntity;
import com.zp.course.pop.DialogFactory;
import com.zp.course.storage.database.AppDatabase;
import com.zp.course.storage.database.dao.CourseDao;
import com.zp.course.storage.database.dao.TimetableDao;
import com.zp.course.storage.database.table.CourseEntity;
import com.zp.course.storage.database.table.CourseInfoEntity;
import com.zp.course.storage.database.table.TimetableEntity;
import com.zp.course.ui.timetable.TimetableAddOrUpdateActivity;
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

    private static final String KEY_ID = "id";
    private static final int REQUEST_CODE = 0x1 << 1;
    private static final int INVALID_POSITION = -1;

    public static void go(Context context) {
        go(context, 0);
    }

    public static void go(Context context, long courseId) {
        Intent intent = new Intent();
        intent.setClass(context, CourseAddOrUpdateActivity.class);
        if (courseId > 0) {
            intent.putExtra(KEY_ID, courseId);
        }
        context.startActivity(intent);
    }

    private EditText mNameText;
    private EditText mNumberText;
    private TextView mTimetableText;
    private TextView mStartText;
    private TextView mEndText;
    private EditText mDescriptionText;
    private CourseDao mCourseDao;
    private CourseAdapter mAdapter;

    private AlertDialog mTimetableDialog;
    private AlertDialog mDeleteDialog;
    private AlertDialog mStartDialog;
    private AlertDialog mEndDialog;
    private long mTimetableId = 0;
    private int mDeletePosition = INVALID_POSITION;
    private String mDeleteTipString;
    private long mUserId;

    private CourseInfoContractEntity mEntity;
    private List<String> mStartList;
    private List<String> mEndList;
    private TimetableDao mTimetableDao;
    private TimetableEntity mTimetableEntity;

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
        mStartText = findViewById(R.id.course_add_start_week);
        mEndText = findViewById(R.id.course_add_end_week);
        mDescriptionText = findViewById(R.id.course_add_description);

        mCourseDao = AppDatabase.getInstance().getCourseDao();

        mTimetableDao = AppDatabase.getInstance().getTimetableDao();
        mUserId = UserManager.getInstance().getUser().getId();
        List<TimetableEntity> timetables = mTimetableDao.getAll(mUserId);
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
            mTimetableEntity = item;
            resetStartList(item.getWeekCount());
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

        mStartList = new ArrayList<>();
        mStartDialog = DialogFactory.createWheelDialog(this, mStartList, (item, position) -> {
            mStartText.setText(item);
            resetEndList(Integer.parseInt(item), mTimetableEntity.getWeekCount());
        }, (dialog, which) -> dialog.dismiss(), (dialog, which) -> {
            mStartText.setText("");
            dialog.dismiss();
        });

        mEndList = new ArrayList<>();
        mEndDialog = DialogFactory.createWheelDialog(this, mEndList, (item, position) -> mEndText.setText(item), (dialog, which) -> dialog.dismiss(), (dialog, which) -> {
            mEndText.setText("");
            dialog.dismiss();
        });

        Intent intent = getIntent();
        if (!intent.hasExtra(KEY_ID)) {
            mEntity = new CourseInfoContractEntity();
            mEntity.setCourse(new CourseEntity());
            return;
        }
        long courseId = intent.getLongExtra(KEY_ID, 0);
        setTitle(R.string.label_course_update);
        mEntity = mCourseDao.findById(courseId);
        mTimetableId = mEntity.getCourse().getTimetableId();
        mTimetableEntity = mTimetableDao.getById(mTimetableId);
        mTimetableText.setText(mTimetableEntity.getName());
        mAdapter.setItems(mEntity.getCourseInfo());
        resetStartList(mTimetableEntity.getWeekCount());
    }

    private void resetStartList(int total) {
        mStartList.clear();
        for (int i = 1; i <= total; i++) {
            mStartList.add(String.valueOf(i));
        }
    }

    private void resetEndList(int start, int end) {
        for (int i = start; i <= end; i++) {
            mEndList.add(String.valueOf(i));
        }
    }

    @Override
    public void onFloatingClick() {
        if (mTimetableId == 0) {
            Toaster.showToast(R.string.prompt_select_timetable_first);
            return;
        }
        CourseInfoAddOrUpdateActivity.goForResult(this, mTimetableId, REQUEST_CODE);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.course_add_timetable_layout:
                mTimetableDialog.show();
                break;
            case R.id.course_add_start_week_layout:
                if (mTimetableId == 0) {
                    Toaster.showToast(R.string.prompt_select_timetable_first);
                    return;
                }
                mStartDialog.show();
                break;
            case R.id.course_add_end_week_layout:
                if (Validator.isEmpty(mStartText.getText())) {
                    Toaster.showToast("请先选择开始的周次。");
                    return;
                }
                mEndDialog.show();
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
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

        if (mTimetableId == 0) {
            Toaster.showToast(R.string.prompt_select_timetable);
            return;
        }

        String description = mDescriptionText.getText().toString();

        CourseEntity entity = mEntity.getCourse();
        entity.setUserId(mUserId);
        entity.setName(name);
        entity.setNumber(number);
        entity.setDescription(description);
        entity.setTimetableId(mTimetableId);


        boolean success = verify(name, number);

//        if (success) {
//            CourseEntity entity = new CourseEntity();
//            entity.setName(name);
//            entity.setNumber(number);
//            entity.setDescription(description);
//            entity.setUserId(UserManager.getInstance().getUser().getId());
//            save(entity);
//        } else {
//            Toaster.showToast(R.string.tip_login_failed);
//        }
    }

    private boolean verify(String name, String number) {
        return mCourseDao.verify(name, number) == 0;
    }

    private void save(CourseEntity entity) {
        mCourseDao.save(entity);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mTimetableId == 0) {
            Toaster.showToast(R.string.prompt_select_timetable_first);
            return;
        }
        CourseInfoEntity entity = mAdapter.getItem(position);
        CourseInfoAddOrUpdateActivity.goForResult(this, mTimetableId, entity, REQUEST_CODE);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        mDeletePosition = position;
        CourseInfoEntity entity = mAdapter.getItem(position);
        String deleteString = String.format(mDeleteTipString, entity.getDayInWeek(), entity.getDescription());
        mDeleteDialog.setMessage(deleteString);
        mDeleteDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (data == null) {
            return;
        }
        CourseInfoEntity entity = data.getParcelableExtra(CourseInfoAddOrUpdateActivity.KEY_ENTITY);
        mAdapter.append(entity);
    }
}
