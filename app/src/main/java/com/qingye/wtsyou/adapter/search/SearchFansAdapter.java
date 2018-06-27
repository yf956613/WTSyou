package com.qingye.wtsyou.adapter.search;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.Fans;
import com.qingye.wtsyou.view.search.SearchFansView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchFansAdapter extends BaseAdapter<Fans,SearchFansView> {

    private SearchFansView.OnItemChildClickListener onItemChildClickListener;

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(SearchFansView.OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public SearchFansAdapter(Activity context) {
        super(context);
    }

    @Override
    public SearchFansView createView(int position, ViewGroup parent) {

        SearchFansView searchFansView = new SearchFansView(context, parent);
        searchFansView.setOnItemChildClickListener(onItemChildClickListener);

        return searchFansView;
    }

}
