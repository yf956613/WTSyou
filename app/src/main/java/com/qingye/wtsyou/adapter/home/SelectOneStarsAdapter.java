package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.EntityStarsItem;
import com.qingye.wtsyou.view.home.SelectOneStarsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SelectOneStarsAdapter extends BaseAdapter<EntityStarsItem,SelectOneStarsView> {

    public SelectOneStarsAdapter(Activity context) {
        super(context);
    }

    @Override
    public SelectOneStarsView createView(int position, ViewGroup parent) {
        return new SelectOneStarsView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
