package com.qingye.wtsyou.adapter.my;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.DeliveryAddress;
import com.qingye.wtsyou.view.my.DeliveryAddressView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class DeliveryAddressAdapter extends BaseAdapter<DeliveryAddress,DeliveryAddressView> {

    public DeliveryAddressAdapter(Activity context) {
        super(context);
    }

    @Override
    public DeliveryAddressView createView(int position, ViewGroup parent) {
        return new DeliveryAddressView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
