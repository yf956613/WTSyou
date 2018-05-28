package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.RankInfos;
import com.qingye.wtsyou.view.home.PastStarsChartsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class PastStarsChartsAdapter extends BaseAdapter<RankInfos,PastStarsChartsView> {

    private PastStarsChartsView.OnItemChildClickListener onItemChildClickListener;

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(PastStarsChartsView.OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public PastStarsChartsAdapter(Activity context) {
        super(context);
    }

    @Override
    public PastStarsChartsView createView(int position, ViewGroup parent) {

        PastStarsChartsView pastStarsChartsView = new PastStarsChartsView(context, parent);
        pastStarsChartsView.setOnItemChildClickListener(onItemChildClickListener);

        return pastStarsChartsView;
    }
}
