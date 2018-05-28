package com.qingye.wtsyou.adapter.home;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.qingye.wtsyou.fragment.home.FragmentFactory;
import com.qingye.wtsyou.model.RecommendStars;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private String[] tabArr = {"1","2","3","4"};
    private List<RecommendStars> recommendStarsList = new ArrayList<>();

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

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