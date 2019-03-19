package com.zp.course.ui.home.fragment;

import com.zp.course.app.RecyclerViewFragment;
import com.zp.course.app.UserManager;
import com.zp.course.storage.database.AppDatabase;
import com.zp.course.storage.database.table.CourseEntity;
import com.zp.course.ui.home.CourseAdapter;
import com.zp.course.util.log.Logger;

import java.util.List;

import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Class description:
 * 全部课程
 *
 * @author zp
 * @version 1.0
 * @see CourseAllFragment
 * @since 2019/3/18
 */
public class CourseAllFragment extends RecyclerViewFragment<CourseEntity> {

    public static CourseAllFragment newInstance() {
        Logger.e("CourseAllFragment");
        return new CourseAllFragment();
    }

    @Override
    protected void onRecyclerViewCreated(RecyclerView view) {
        view.setLayoutManager(new LinearLayoutManager(requireContext()));
        view.setItemAnimator(new DefaultItemAnimator());

        CourseAdapter adapter = new CourseAdapter(null);
        setAdapter(adapter);
        obtainData();
    }

    private void obtainData() {
        List<CourseEntity> list = AppDatabase.getInstance().getCourseDao().getAll(UserManager.getInstance().getUser().getId());
        getAdapter().setItems(list);
    }
}
