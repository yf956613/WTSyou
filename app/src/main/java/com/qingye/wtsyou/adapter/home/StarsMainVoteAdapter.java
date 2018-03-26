package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Campaign;
import com.qingye.wtsyou.view.home.StarsMainVoteView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsMainVoteAdapter extends BaseAdapter<Campaign,StarsMainVoteView> {

    public StarsMainVoteAdapter(Activity context) {
        super(context);
    }

    @Override
    public StarsMainVoteView createView(int position, ViewGroup parent) {
        return new StarsMainVoteView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
