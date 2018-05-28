package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.Campaign;
import com.qingye.wtsyou.view.home.StarsMainShowView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsMainShowAdapter extends BaseAdapter<Campaign,StarsMainShowView> {

    public StarsMainShowAdapter(Activity context) {
        super(context);
    }

    @Override
    public StarsMainShowView createView(int position, ViewGroup parent) {
        return new StarsMainShowView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
