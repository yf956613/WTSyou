package com.qingye.wtsyou.adapter.my;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Recharge;
import com.qingye.wtsyou.view.my.RechargeView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class RechargeAdapter extends BaseAdapter<Recharge,RechargeView> {

    public RechargeAdapter(Activity context) {
        super(context);
    }

    @Override
    public RechargeView createView(int position, ViewGroup parent) {
        return new RechargeView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
