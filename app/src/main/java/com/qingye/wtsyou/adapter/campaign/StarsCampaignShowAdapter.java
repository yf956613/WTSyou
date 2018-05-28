package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.Concert;
import com.qingye.wtsyou.view.campaign.StarsCampaignShowView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsCampaignShowAdapter extends BaseAdapter<Concert,StarsCampaignShowView> {

    public StarsCampaignShowAdapter(Activity context) {
        super(context);
    }

    @Override
    public StarsCampaignShowView createView(int position, ViewGroup parent) {
        return new StarsCampaignShowView(context, parent);
    }
}
