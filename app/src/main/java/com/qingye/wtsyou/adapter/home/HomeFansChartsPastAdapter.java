package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.EntityRankInfo;
import com.qingye.wtsyou.view.home.HomeFansChartsPastView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HomeFansChartsPastAdapter extends BaseAdapter<EntityRankInfo,HomeFansChartsPastView> {

    public HomeFansChartsPastAdapter(Activity context) {
        super(context);
    }

    @Override
    public HomeFansChartsPastView createView(int position, ViewGroup parent) {
        return new HomeFansChartsPastView(context, parent);
    }
}
