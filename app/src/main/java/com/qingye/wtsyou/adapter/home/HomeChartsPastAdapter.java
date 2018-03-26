package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Charts;
import com.qingye.wtsyou.view.home.HomeChartsPastView;
import com.qingye.wtsyou.view.home.HomeChartsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HomeChartsPastAdapter extends BaseAdapter<Charts,HomeChartsPastView> {

    public HomeChartsPastAdapter(Activity context) {
        super(context);
    }

    @Override
    public HomeChartsPastView createView(int position, ViewGroup parent) {
        return new HomeChartsPastView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
