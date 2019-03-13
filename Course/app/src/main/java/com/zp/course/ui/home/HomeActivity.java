package com.zp.course.ui.home;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zp.course.R;
import com.zp.course.app.ToolbarActivity;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see HomeActivity
 * @since 2019/3/5
 */
public class HomeActivity extends ToolbarActivity {

    public static void go(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_layout);
    }
}
