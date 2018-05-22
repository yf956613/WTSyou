package com.qingye.wtsyou.adapter.my;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.HeartDetailed;
import com.qingye.wtsyou.modle.Stars;
import com.qingye.wtsyou.view.home.FansMainIdolView;
import com.qingye.wtsyou.view.my.HeartDetailedView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HeartDetailedAdapter extends BaseAdapter<HeartDetailed,HeartDetailedView> {

    public HeartDetailedAdapter(Activity context) {
        super(context);
    }

    @Override
    public HeartDetailedView createView(int position, ViewGroup parent) {
        return new HeartDetailedView(context, parent);
    }

}
