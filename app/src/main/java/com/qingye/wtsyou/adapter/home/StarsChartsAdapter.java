package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.RankInfos;
import com.qingye.wtsyou.view.home.StarsChartsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsChartsAdapter extends BaseAdapter<RankInfos,StarsChartsView> {

    private StarsChartsView.OnItemChildClickListener onItemChildClickListener;

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(StarsChartsView.OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public StarsChartsAdapter(Activity context) {
        super(context);
    }

    @Override
    public StarsChartsView createView(int position, ViewGroup parent) {

        StarsChartsView starsChartsView = new StarsChartsView(context, parent);
        starsChartsView.setOnItemChildClickListener(onItemChildClickListener);

        return starsChartsView;
    }
}
