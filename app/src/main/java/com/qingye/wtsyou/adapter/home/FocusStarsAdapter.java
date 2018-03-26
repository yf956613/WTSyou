package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Stars;
import com.qingye.wtsyou.view.home.FocusStarsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class FocusStarsAdapter extends BaseAdapter<Stars,FocusStarsView> {

    public FocusStarsAdapter(Activity context) {
        super(context);
    }

    @Override
    public FocusStarsView createView(int position, ViewGroup parent) {
        return new FocusStarsView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
