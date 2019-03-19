package com.zp.course.ui.course;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zp.course.R;
import com.zp.course.app.ToolbarActivity;
import com.zp.course.app.UserManager;
import com.zp.course.storage.database.AppDatabase;
import com.zp.course.storage.database.dao.CourseDao;
import com.zp.course.storage.database.table.CourseEntity;
import com.zp.course.util.Toaster;
import com.zp.course.util.Validator;

import androidx.annotation.Nullable;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see CourseAddActivity
 * @since 2019/3/15
 */
public class CourseAddActivity extends ToolbarActivity {

    public static void go(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CourseAddActivity.class);
        context.startActivity(intent);
    }

    private EditText mNameText;
    private EditText mNumberText;
    private EditText mDescriptionText;
    private CourseDao mCourseDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add_layout);
        showFloatingButton(true);
        initView();
    }

    private void initView() {
        mNameText = findViewById(R.id.course_add_name);
        mNumberText = findViewById(R.id.course_add_number);
        mDescriptionText = findViewById(R.id.course_add_description);

        mCourseDao = AppDatabase.getInstance().getCourseDao();
    }


    public void onClick(View view) {
        attemptSave();
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
}
