package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Campaign;
import com.qingye.wtsyou.view.campaign.ActivityOfficialView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ActivityOfficialAdapter extends BaseAdapter<Campaign,ActivityOfficialView> {

    public ActivityOfficialAdapter(Activity context) {
        super(context);
    }

    @Override
    public ActivityOfficialView createView(int position, ViewGroup parent) {
        return new ActivityOfficialView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
