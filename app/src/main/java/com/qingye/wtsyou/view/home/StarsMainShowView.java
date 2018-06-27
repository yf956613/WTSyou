package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.Activitys;

import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsMainShowView extends BaseView<Activitys> implements View.OnClickListener {

    public StarsMainShowView(android.app.Activity context, ViewGroup parent) {
        super(context, R.layout.list_stars_main_show, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        return super.createView();
    }

    @Override
    public void bindView(Activitys data_){
        super.bindView(data_ != null ? data_ : new Activitys());
    }

    @Override
    public void onClick(View v) {

    }
}
