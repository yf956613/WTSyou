package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.EntityRankInfo;
import com.qingye.wtsyou.view.home.HomeStarsChartsPastView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HomeStarsChartsPastAdapter extends BaseAdapter<EntityRankInfo,HomeStarsChartsPastView> {

    public HomeStarsChartsPastAdapter(Activity context) {
        super(context);
    }

    @Override
    public HomeStarsChartsPastView createView(int position, ViewGroup parent) {
        return new HomeStarsChartsPastView(context, parent);
    }
}
