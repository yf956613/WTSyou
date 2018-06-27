package com.qingye.wtsyou.adapter.home;

import android.view.ViewGroup;

import com.qingye.wtsyou.model.Activitys;
import com.qingye.wtsyou.view.home.StarsMainShowView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsMainShowAdapter extends BaseAdapter<Activitys,StarsMainShowView> {

    public StarsMainShowAdapter(android.app.Activity context) {
        super(context);
    }

    @Override
    public StarsMainShowView createView(int position, ViewGroup parent) {
        return new StarsMainShowView(context, parent);
    }

}
