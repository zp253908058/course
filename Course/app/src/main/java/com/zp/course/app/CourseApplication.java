package com.zp.course.app;

import android.app.Application;

import com.zp.course.util.Toaster;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see CourseApplication
 * @since 2019/3/11
 */
public class CourseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Toaster.init(this);
    }
}
