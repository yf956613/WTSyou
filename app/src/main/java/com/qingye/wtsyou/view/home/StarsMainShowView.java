package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.Campaign;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsMainShowView extends BaseView<Campaign> implements View.OnClickListener {

    public StarsMainShowView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_stars_main_show, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        return super.createView();
    }

    @Override
    public void bindView(Campaign data_){
        super.bindView(data_ != null ? data_ : new Campaign());
    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}