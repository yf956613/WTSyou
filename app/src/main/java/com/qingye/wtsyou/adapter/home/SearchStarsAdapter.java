package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Stars;
import com.qingye.wtsyou.view.home.SearchStarsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchStarsAdapter extends BaseAdapter<Stars,SearchStarsView> {

    public SearchStarsAdapter(Activity context) {
        super(context);
    }

    @Override
    public SearchStarsView createView(int position, ViewGroup parent) {
        return new SearchStarsView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
