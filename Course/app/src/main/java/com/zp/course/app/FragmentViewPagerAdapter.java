package com.zp.course.app;

import com.zp.course.util.CollectionsUtils;
import com.zp.course.util.Preconditions;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see FragmentViewPagerAdapter
 * @since 2019/3/18
 */
public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {

    public interface Callback {
        Fragment getItem(int position);

        int getCount();

        CharSequence getPageTitle(int position);
    }

    private Callback mCallback;

    public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
        super(fm);
        mCallback = new CallbackImpl(fragments, titles);
    }

    public FragmentViewPagerAdapter(FragmentManager fm, Callback callback) {
        super(fm);
        mCallback = Preconditions.checkNotNull(callback);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public Fragment getItem(int position) {
        return mCallback.getItem(position);
    }

    @Override
    public int getCount() {
        return mCallback.getCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCallback.getPageTitle(position);
    }

    private static final class CallbackImpl implements Callback {
        private List<Fragment> mFragments;
        private String[] mTitles;

        private CallbackImpl(List<Fragment> fragments, String[] titles) {
            mFragments = fragments;
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return CollectionsUtils.get(mFragments, position);
        }

        @Override
        public int getCount() {
            return CollectionsUtils.sizeOf(mFragments);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
