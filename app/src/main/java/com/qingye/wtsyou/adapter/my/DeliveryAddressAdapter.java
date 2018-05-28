package com.qingye.wtsyou.adapter.my;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.DeliveryAddress;
import com.qingye.wtsyou.view.my.DeliveryAddressView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class DeliveryAddressAdapter extends BaseAdapter<DeliveryAddress,DeliveryAddressView> {

    private DeliveryAddressView.OnItemChildClickListener onItemChildClickListener;

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(DeliveryAddressView.OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public DeliveryAddressAdapter(Activity context) {
        super(context);
    }

    @Override
    public DeliveryAddressView createView(int position, ViewGroup parent) {

        DeliveryAddressView deliveryAddressView = new DeliveryAddressView(context, parent);
        deliveryAddressView.setOnItemChildClickListener(onItemChildClickListener);

        return deliveryAddressView;
    }

}
