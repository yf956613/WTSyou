package com.qingye.wtsyou.adapter.home;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qingye.wtsyou.fragment.home.FragmentFactory;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private String[] tabArr = {"1","2","3","4","5"};

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return FragmentFactory.create(position);
    }

    @Override
    public int getCount() {
        return tabArr.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabArr[position];
    }
}