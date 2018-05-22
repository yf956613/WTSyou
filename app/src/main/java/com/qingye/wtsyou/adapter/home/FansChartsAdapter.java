package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.FansCharts;
import com.qingye.wtsyou.view.home.FansChartsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class FansChartsAdapter extends BaseAdapter<FansCharts,FansChartsView> {

    public FansChartsAdapter(Activity context) {
        super(context);
    }

    @Override
    public FansChartsView createView(int position, ViewGroup parent) {
        return new FansChartsView(context, parent);
    }

}
