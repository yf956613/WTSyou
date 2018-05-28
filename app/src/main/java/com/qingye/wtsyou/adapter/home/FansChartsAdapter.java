package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.RankInfos;
import com.qingye.wtsyou.view.home.FansChartsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class FansChartsAdapter extends BaseAdapter<RankInfos,FansChartsView> {

    private FansChartsView.OnItemChildClickListener onItemChildClickListener;

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(FansChartsView.OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public FansChartsAdapter(Activity context) {
        super(context);
    }

    @Override
    public FansChartsView createView(int position, ViewGroup parent) {
        FansChartsView fansChartsView = new FansChartsView(context, parent);
        fansChartsView.setOnItemChildClickListener(onItemChildClickListener);

        return fansChartsView;
    }

}
