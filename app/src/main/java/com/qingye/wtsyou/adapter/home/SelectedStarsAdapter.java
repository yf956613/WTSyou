package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Stars;
import com.qingye.wtsyou.view.home.SelectedStarsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SelectedStarsAdapter extends BaseAdapter<Stars,SelectedStarsView> {

    public SelectedStarsAdapter(Activity context) {
        super(context);
    }

    @Override
    public SelectedStarsView createView(int position, ViewGroup parent) {
        return new SelectedStarsView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
