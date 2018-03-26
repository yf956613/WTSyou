package com.qingye.wtsyou.adapter.activity;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Campaign;
import com.qingye.wtsyou.view.activity.ActivityHotShowView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ActivityHotShowAdapter extends BaseAdapter<Campaign,ActivityHotShowView> {

    public ActivityHotShowAdapter(Activity context) {
        super(context);
    }

    @Override
    public ActivityHotShowView createView(int position, ViewGroup parent) {
        return new ActivityHotShowView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}