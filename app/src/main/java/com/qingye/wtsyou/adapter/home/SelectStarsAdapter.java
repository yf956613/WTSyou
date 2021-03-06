package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.EntityStars;
import com.qingye.wtsyou.view.home.SelectStarsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SelectStarsAdapter extends BaseAdapter<EntityStars,SelectStarsView> {

    public SelectStarsAdapter(Activity context) {
        super(context);
    }

    @Override
    public SelectStarsView createView(int position, ViewGroup parent) {
        return new SelectStarsView(context, parent);
    }
}
