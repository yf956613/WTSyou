package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.StarsCharts;
import com.qingye.wtsyou.view.home.StarsChartsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsChartsAdapter extends BaseAdapter<StarsCharts,StarsChartsView> {

    public StarsChartsAdapter(Activity context) {
        super(context);
    }

    @Override
    public StarsChartsView createView(int position, ViewGroup parent) {
        return new StarsChartsView(context, parent);
    }
}
