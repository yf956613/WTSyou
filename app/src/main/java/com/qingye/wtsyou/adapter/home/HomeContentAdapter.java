package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.EntityHomeContent;
import com.qingye.wtsyou.view.home.HomeContentView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HomeContentAdapter extends BaseAdapter<EntityHomeContent,HomeContentView> {

    public HomeContentAdapter(Activity context) {
        super(context);
    }

    @Override
    public HomeContentView createView(int position, ViewGroup parent) {
        return new HomeContentView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
