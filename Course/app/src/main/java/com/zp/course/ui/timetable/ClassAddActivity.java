package com.zp.course.ui.timetable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zp.course.R;
import com.zp.course.app.ToolbarActivity;

import androidx.annotation.Nullable;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see ClassAddActivity
 * @since 2019/3/21
 */
public class ClassAddActivity extends ToolbarActivity {
    private static final String KEY_ID = "id";

    public static void go(Context context, long id) {
        Intent intent = new Intent();
        intent.setClass(context, TimetableAddActivity.class);
        intent.putExtra(KEY_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_add_layout);

    }
}
