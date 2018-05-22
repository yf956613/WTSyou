package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.DeliveryAddress;
import com.qingye.wtsyou.view.campaign.SelectAddressView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SelectAddressAdapter extends BaseAdapter<DeliveryAddress,SelectAddressView> {

    private SelectAddressView.OnItemChildClickListener onItemChildClickListener;

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(SelectAddressView.OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public SelectAddressAdapter(Activity context) {
        super(context);
    }

    @Override
    public SelectAddressView createView(int position, ViewGroup parent) {
        SelectAddressView selectAddressView = new SelectAddressView(context, parent);
        selectAddressView.setOnItemChildClickListener(onItemChildClickListener);

        return selectAddressView;
    }
}
