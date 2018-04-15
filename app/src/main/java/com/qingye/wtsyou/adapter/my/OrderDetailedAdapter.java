package com.qingye.wtsyou.adapter.my;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.OrderDetailed;
import com.qingye.wtsyou.view.my.OrderDetailedView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class OrderDetailedAdapter extends BaseAdapter<OrderDetailed,OrderDetailedView> {

    public OrderDetailedAdapter(Activity context) {
        super(context);
    }

    @Override
    public OrderDetailedView createView(int position, ViewGroup parent) {
        return new OrderDetailedView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
