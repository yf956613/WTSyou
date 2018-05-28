package com.qingye.wtsyou.adapter.search;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.City;
import com.qingye.wtsyou.view.search.SearchCityView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchCityAdapter extends BaseAdapter<City,SearchCityView > {

    public SearchCityAdapter(Activity context) {
        super(context);
    }

    public SearchCityView createView(int position, ViewGroup parent) {
        return new SearchCityView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

}
