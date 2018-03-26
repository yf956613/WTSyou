package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.Charts;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HomeChartsView extends BaseView<Charts> implements View.OnClickListener {

    public HomeChartsView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_newest_charts, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        return super.createView();
    }

    @Override
    public void bindView(Charts data_){
        super.bindView(data_ != null ? data_ : new Charts());
    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
