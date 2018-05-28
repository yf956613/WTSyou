package com.qingye.wtsyou.adapter.my;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.DiamondDetailed;
import com.qingye.wtsyou.view.my.DiamondDetailedView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class DiamondDetailedAdapter extends BaseAdapter<DiamondDetailed,DiamondDetailedView> {

    public DiamondDetailedAdapter(Activity context) {
        super(context);
    }

    @Override
    public DiamondDetailedView createView(int position, ViewGroup parent) {
        return new DiamondDetailedView(context, parent);
    }
}
