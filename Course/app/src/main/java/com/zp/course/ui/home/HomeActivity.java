package com.zp.course.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.zp.course.R;
import com.zp.course.app.BaseActivity;
import com.zp.course.app.FragmentViewPagerAdapter;
import com.zp.course.app.UserManager;
import com.zp.course.pop.DialogFactory;
import com.zp.course.storage.database.AppDatabase;
import com.zp.course.storage.database.dao.TimetableDao;
import com.zp.course.storage.database.table.TimetableEntity;
import com.zp.course.ui.course.CourseAddActivity;
import com.zp.course.ui.home.fragment.CourseAllFragment;
import com.zp.course.ui.home.fragment.CourseLateFragment;
import com.zp.course.ui.timetable.TimetableActivity;
import com.zp.course.ui.timetable.TimetableAddOrUpdateActivity;
import com.zp.course.util.Toaster;
import com.zp.course.util.Validator;
import com.zp.course.util.log.Logger;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see HomeActivity
 * @since 2019/3/5
 */
public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentViewPagerAdapter.Callback {

    public static void go(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        context.startActivity(intent);
    }

    private long mLastPressedTime = 0;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);

        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.text_navigation_drawer_open, R.string.text_navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.main_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mActionButton = findViewById(R.id.main_floating_action_button);


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), this));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    fadedOut(positionOffset);
                } else {
                    fadedIn(positionOffset);
                }
                Logger.e(String.valueOf(positionOffset));
            }

            @Override
            public void onPageSelected(int position) {
                mActionButton.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    private void fadedIn(float offset) {
        ViewCompat.animate(mActionButton).scaleX(offset).scaleX(offset).start();
    }

    private void fadedOut(float offset) {
        ViewCompat.animate(mActionButton).scaleX(offset).scaleX(offset).start();
    }

    public void onClick(View view) {
        TimetableDao dao = AppDatabase.getInstance().getTimetableDao();
        long userId = UserManager.getInstance().getUser().getId();
        List<TimetableEntity> list = dao.getAll(userId);
        if (Validator.isEmpty(list)) {
            AlertDialog dialog = DialogFactory.createAlertDialog(this, "你还没有添加课表， 请先添加课表", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TimetableAddOrUpdateActivity.go(HomeActivity.this);
                }
            });
            dialog.show();
            return;
        }
        CourseAddActivity.go(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        long currentTime = System.currentTimeMillis();//获取第一次按键时间
        if (currentTime - mLastPressedTime > 2000) {
            Toaster.showToast("再按一次退出程序");
            mLastPressedTime = currentTime;
        } else {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_course:
                TimetableActivity.go(this);
                break;
        }
        return false;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = CourseAllFragment.newInstance();
                break;
            case 1:
                fragment = CourseLateFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position) {
            case 0:
                title = "全部";
                break;
            case 1:
                title = "最近";
                break;
        }
        return title;
    }
}
