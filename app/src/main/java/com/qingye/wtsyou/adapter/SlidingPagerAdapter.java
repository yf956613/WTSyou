package com.qingye.wtsyou.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by pm89 on 2018/4/3.
 */

public class SlidingPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments;
    private String[] mTitles_3;
    private Context activity;

    public SlidingPagerAdapter(FragmentManager fm, ArrayList<Fragment> mFragments, String[] mTitles_3, Context activity) {
        super(fm);
        this.mFragments = mFragments;
        this.mTitles_3 = mTitles_3;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles_3[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
