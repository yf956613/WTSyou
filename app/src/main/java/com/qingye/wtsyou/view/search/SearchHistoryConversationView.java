package com.qingye.wtsyou.view.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.HotTopic;

import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchHistoryConversationView extends BaseView<HotTopic> implements View.OnClickListener {

    public SearchHistoryConversationView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_search_conversation_history, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        return super.createView();
    }

    @Override
    public void bindView(HotTopic data_){
        super.bindView(data_ != null ? data_ : new HotTopic());
    }

    @Override
    public void onClick(View v) {

    }
}
