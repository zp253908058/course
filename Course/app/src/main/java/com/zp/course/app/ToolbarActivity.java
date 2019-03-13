package com.zp.course.app;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;

import com.zp.course.R;
import com.zp.course.util.Validator;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see ToolbarActivity
 * @since 2019/3/6
 */
public abstract class ToolbarActivity extends BaseActivity {

    private ViewStub mContentView;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.setContentView(R.layout.activity_toolbar_layout);

        initView();
    }

    private void initView() {
        mContentView = findViewById(R.id.toolbar_content_layout);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //Toolbar 初始化 获取toolbar的方式是getSupportActionBar()
        //有些操作通过ActionBar来设置也是一样的，注意要在setSupportActionBar(toolbar);之后，不然就报错了
        Drawable drawable = mToolbar.getNavigationIcon();
        if (drawable != null) {
            DrawableCompat.setTint(drawable, ResourcesCompat.getColor(getResources(), android.R.color.white, null));
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void showNavigationIcon(boolean isShow) {
        ActionBar bar = getSupportActionBar();
        if (Validator.isNotNull(bar)) {
            bar.setDisplayHomeAsUpEnabled(isShow);
            bar.setHomeButtonEnabled(isShow);
        }
    }

    public void setNavigationIcon(@DrawableRes int resId) {
        mToolbar.setNavigationIcon(resId);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mContentView.setLayoutResource(layoutResID);
        mContentView.inflate();
    }

    @Override
    public void setContentView(View view) {

    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {

    }
}
