package com.qingye.wtsyou.view.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.CoinDetailed;
import com.qingye.wtsyou.modle.HeartDetailed;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class CoinDetailedView extends BaseView<CoinDetailed> implements View.OnClickListener {

    public CoinDetailedView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_coin_detailed, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        return super.createView();
    }

    @Override
    public void bindView(CoinDetailed data_){
        super.bindView(data_ != null ? data_ : new CoinDetailed());

    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
