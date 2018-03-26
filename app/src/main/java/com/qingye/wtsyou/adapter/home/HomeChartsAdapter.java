package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Charts;
import com.qingye.wtsyou.view.home.HomeChartsView;
import com.qingye.wtsyou.view.home.HomeContentView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HomeChartsAdapter extends BaseAdapter<Charts,HomeChartsView> {

    public HomeChartsAdapter(Activity context) {
        super(context);
    }

    @Override
    public HomeChartsView createView(int position, ViewGroup parent) {
        return new HomeChartsView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}