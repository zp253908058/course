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

        ImageView imageView = findViewById(R.id.image_view);

        AnimationDrawable drawable = (AnimationDrawable) imageView.getDrawable();
        drawable.start();
//
//
        TextView text = findViewById(R.id.alpha_text_view);
//
//        AnimationSet set = new AnimationSet(true);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0);
//
//        ScaleAnimation scaleAnimation = new ScaleAnimation(this, getResources().getAnimation(R.anim.anim_scale_test));
//
////        TranslateAnimation translateAnimation = new TranslateAnimation();
//        set.addAnimation(alphaAnimation);
//        set.addAnimation(scaleAnimation);
//
//
//        set.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
////                text.setVisibility(View.GONE);
//
//                Log.e("", "end ..............");
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        set.setDuration(2000);
//        set.setInterpolator(new LinearInterpolator());
//        text.setAnimation(set);
//        set.start();

        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(text, "text", new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                return startValue + (endValue - startValue) * fraction;
            }
        }, 0, 100);
        objectAnimator.start();
    }
}
