package com.qingye.wtsyou.adapter.activity;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Campaign;
import com.qingye.wtsyou.view.activity.StarsCampaignShowView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsCampaignShowAdapter extends BaseAdapter<Campaign,StarsCampaignShowView> {

    public StarsCampaignShowAdapter(Activity context) {
        super(context);
    }

    @Override
    public StarsCampaignShowView createView(int position, ViewGroup parent) {
        return new StarsCampaignShowView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}