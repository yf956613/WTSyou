package com.qingye.wtsyou.view.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.CrowdMoneyDetailed;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class CrowdMoneyDetailedView extends BaseView<CrowdMoneyDetailed> implements View.OnClickListener {

    public CrowdMoneyDetailedView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_crowd_money_detailed, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        return super.createView();
    }

    @Override
    public void bindView(CrowdMoneyDetailed data_){
        super.bindView(data_ != null ? data_ : new CrowdMoneyDetailed());

    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
