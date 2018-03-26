package com.qingye.wtsyou.view.activity;

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

public class ActivityNewSupportView extends BaseView<Campaign> implements View.OnClickListener {

    private ProgressBar joinProgressBar;//参与人数进度条
    public ActivityNewSupportView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_activity_support, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        joinProgressBar = findViewById(R.id.join_progressbar);

        return super.createView();
    }

    @Override
    public void bindView(Campaign data_){
        super.bindView(data_ != null ? data_ : new Campaign());
        joinProgressBar.setProgress((position + 1) * 20);
    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
