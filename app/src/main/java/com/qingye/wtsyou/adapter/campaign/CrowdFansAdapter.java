package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Fans;
import com.qingye.wtsyou.view.campaign.CrowdFansView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class CrowdFansAdapter extends BaseAdapter<Fans,CrowdFansView> {

    public CrowdFansAdapter(Activity context) {
        super(context);
    }

    @Override
    public CrowdFansView createView(int position, ViewGroup parent) {
        return new CrowdFansView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}