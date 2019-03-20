package com.zp.course.ui.timetable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zp.course.R;
import com.zp.course.app.ToolbarActivity;
import com.zp.course.app.UserManager;
import com.zp.course.storage.database.AppDatabase;
import com.zp.course.storage.database.dao.TimetableDao;
import com.zp.course.storage.database.table.TimetableEntity;
import com.zp.course.ui.sign.LoginActivity;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Class description:
 * 课表页面
 *
 * @author zp
 * @version 1.0
 * @see TimetableActivity
 * @since 2019/3/20
 */
public class TimetableActivity extends ToolbarActivity {

    public static void go(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TimetableActivity.class);
        context.startActivity(intent);
    }

    private TimetableAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_layout);

        initView();
    }

    private void initView() {
        RecyclerView view = findViewById(R.id.recycler_view);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setItemAnimator(new DefaultItemAnimator());
        view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new TimetableAdapter(null);
        view.setAdapter(mAdapter);


        obtainData();
    }

    private void obtainData() {
        TimetableDao dao = AppDatabase.getInstance().getTimetableDao();
        List<TimetableEntity> list = dao.getAll(UserManager.getInstance().getUser().getId());
        mAdapter.setItems(list);
    }

    public void onClick(View view) {
        TimetableAddActivity.go(this);
    }
}
