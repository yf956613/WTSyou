package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.Crowd;
import com.qingye.wtsyou.view.home.StarsMainCrowdView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsMainCrowdAdapter extends BaseAdapter<Crowd,StarsMainCrowdView> {

    public StarsMainCrowdAdapter(Activity context) {
        super(context);
    }

    @Override
    public StarsMainCrowdView createView(int position, ViewGroup parent) {
        return new StarsMainCrowdView(context, parent);
    }

}
