package com.qingye.wtsyou.adapter.search;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.HotTopic;
import com.qingye.wtsyou.view.search.SearchHistoryConversationView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchHistoryConversationAdapter extends BaseAdapter<HotTopic,SearchHistoryConversationView> {

    public SearchHistoryConversationAdapter(Activity context) {
        super(context);
    }

    @Override
    public SearchHistoryConversationView createView(int position, ViewGroup parent) {
        return new SearchHistoryConversationView(context, parent);
    }

}
