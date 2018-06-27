package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.FansIdol;
import com.qingye.wtsyou.view.home.FansMainIdolView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class FansMainIdolAdapter extends BaseAdapter<FansIdol,FansMainIdolView> {

    public FansMainIdolAdapter(Activity context) {
        super(context);
    }

    @Override
    public FansMainIdolView createView(int position, ViewGroup parent) {
        return new FansMainIdolView(context, parent);
    }

}
