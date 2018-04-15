package com.qingye.wtsyou.view.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.DiamondDetailed;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class DiamondDetailedView extends BaseView<DiamondDetailed> implements View.OnClickListener {

    public DiamondDetailedView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_diamond_detailed, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        return super.createView();
    }

    @Override
    public void bindView(DiamondDetailed data_){
        super.bindView(data_ != null ? data_ : new DiamondDetailed());

    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
