package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Campaign;
import com.qingye.wtsyou.view.campaign.ActivityNewSupportView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ActivityNewSupportAdapter extends BaseAdapter<Campaign,ActivityNewSupportView> {

    public ActivityNewSupportAdapter(Activity context) {
        super(context);
    }

    @Override
    public ActivityNewSupportView createView(int position, ViewGroup parent) {
        return new ActivityNewSupportView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
