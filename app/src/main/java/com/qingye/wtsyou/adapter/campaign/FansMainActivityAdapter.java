package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.Activitys;
import com.qingye.wtsyou.view.campaign.FansMainActivityView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class FansMainActivityAdapter extends BaseAdapter<Activitys,FansMainActivityView> {

    public FansMainActivityAdapter(Activity context) {
        super(context);
    }

    @Override
    public FansMainActivityView createView(int position, ViewGroup parent) {
        return new FansMainActivityView(context, parent);
    }

}
