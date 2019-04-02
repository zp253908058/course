package com.zp.course.ui.course;

import android.app.Activity;
import android.content.Intent;

import com.zp.course.app.ToolbarActivity;
import com.zp.course.ui.timetable.TimetableAddOrUpdateActivity;

/**
 * Class description:
 *
 * @author ZP
 * @version 1.0
 * @see CourseInfoAddOrUpdateActivity
 * @since 2019/4/2
 */
public class CourseInfoAddOrUpdateActivity extends ToolbarActivity {
    private static final String KEY_ID = "id";

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
}
