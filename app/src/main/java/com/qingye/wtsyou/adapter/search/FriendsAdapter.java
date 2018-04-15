package com.qingye.wtsyou.adapter.search;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Fans;
import com.qingye.wtsyou.view.my.FriendsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class FriendsAdapter extends BaseAdapter<Fans,FriendsView> {

    public FriendsAdapter(Activity context) {
        super(context);
    }

    @Override
    public FriendsView createView(int position, ViewGroup parent) {
        return new FriendsView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
