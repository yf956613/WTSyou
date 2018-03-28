package com.qingye.wtsyou.adapter.search;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Fans;
import com.qingye.wtsyou.view.search.SearchFansView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchFansAdapter extends BaseAdapter<Fans,SearchFansView> {

    public SearchFansAdapter(Activity context) {
        super(context);
    }

    @Override
    public SearchFansView createView(int position, ViewGroup parent) {
        return new SearchFansView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
