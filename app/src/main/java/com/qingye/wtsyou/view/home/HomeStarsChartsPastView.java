package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.StarsCharts;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HomeStarsChartsPastView extends BaseView<StarsCharts> implements View.OnClickListener {

    public HomeStarsChartsPastView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_past_stars_charts, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        return super.createView();
    }

    @Override
    public void bindView(StarsCharts data_){
        super.bindView(data_ != null ? data_ : new StarsCharts());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
