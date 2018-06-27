package com.qingye.wtsyou.adapter.search;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.HotTopic;
import com.qingye.wtsyou.view.search.SearchHotConversationView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchHotConversationAdapter extends BaseAdapter<HotTopic,SearchHotConversationView> {

    public SearchHotConversationAdapter(Activity context) {
        super(context);
    }

    @Override
    public SearchHotConversationView createView(int position, ViewGroup parent) {
        return new SearchHotConversationView(context, parent);
    }

}
