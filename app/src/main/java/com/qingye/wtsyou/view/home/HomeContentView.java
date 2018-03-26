package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.Campaign;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.model.Entry;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HomeContentView extends BaseView<Campaign> implements View.OnClickListener {

    public HomeContentView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_home_content, parent);
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
