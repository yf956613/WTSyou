package com.qingye.wtsyou.adapter.my;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.EntityOrder;
import com.qingye.wtsyou.view.my.OrderView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class OrderDetailedAdapter extends BaseAdapter<EntityOrder,OrderView> {

    private OrderView.OnItemChildClickListener onItemChildClickListener;

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(OrderView.OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public OrderDetailedAdapter(Activity context) {
        super(context);
    }

    @Override
    public OrderView createView(int position, ViewGroup parent) {

        OrderView orderDetailedView = new OrderView(context, parent);
        orderDetailedView.setOnItemChildClickListener(onItemChildClickListener);

        return orderDetailedView;
    }

}
