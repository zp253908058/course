package com.zp.course.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zp.course.R;
import com.zp.course.util.Preconditions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see RecyclerViewFragment
 * @since 2019/3/18
 */
public abstract class RecyclerViewFragment<T> extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter<T> mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_recycler_view, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        onRecyclerViewCreated(mRecyclerView);
    }

    protected abstract void onRecyclerViewCreated(RecyclerView view);

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setAdapter(RecyclerAdapter<T> adapter) {
        mAdapter = adapter;
        Preconditions.checkNotNull(adapter);
        mRecyclerView.setAdapter(mAdapter);
    }

    public RecyclerAdapter<T> getAdapter() {
        return mAdapter;
    }

    protected void setItemAnimator(@Nullable RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    protected void setLayoutManager(@Nullable RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);
    }

    protected void addItemDecoration(@NonNull RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor);
    }
}
