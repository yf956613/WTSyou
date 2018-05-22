package com.qingye.wtsyou.adapter.search;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.SearchContent;
import com.qingye.wtsyou.view.search.SearchHotConversationView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchHotConversationAdapter extends BaseAdapter<SearchContent,SearchHotConversationView> {

    public SearchHotConversationAdapter(Activity context) {
        super(context);
    }

    @Override
    public SearchHotConversationView createView(int position, ViewGroup parent) {
        return new SearchHotConversationView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
