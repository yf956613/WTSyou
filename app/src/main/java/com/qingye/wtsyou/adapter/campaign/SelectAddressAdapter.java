package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.DeliveryAddress;
import com.qingye.wtsyou.view.campaign.SelectAddressView;
import com.qingye.wtsyou.view.my.DeliveryAddressView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SelectAddressAdapter extends BaseAdapter<DeliveryAddress,SelectAddressView> {

    public SelectAddressAdapter(Activity context) {
        super(context);
    }

    @Override
    public SelectAddressView createView(int position, ViewGroup parent) {
        return new SelectAddressView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
