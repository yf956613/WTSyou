package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.Fans;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchFansView extends BaseView<Fans> implements View.OnClickListener {

    public SearchFansView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_search_fans, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        return super.createView();
    }

    @Override
    public void bindView(Fans data_){
        super.bindView(data_ != null ? data_ : new Fans());
    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
