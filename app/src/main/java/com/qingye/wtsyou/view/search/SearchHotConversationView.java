package com.qingye.wtsyou.view.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.SearchContent;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchHotConversationView extends BaseView<SearchContent> implements View.OnClickListener {

    public SearchHotConversationView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_search_conversation_search_hot, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        return super.createView();
    }

    @Override
    public void bindView(SearchContent data_){
        super.bindView(data_ != null ? data_ : new SearchContent());
    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
