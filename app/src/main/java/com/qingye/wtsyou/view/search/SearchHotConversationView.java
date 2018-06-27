package com.qingye.wtsyou.view.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.HotTopic;

import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchHotConversationView extends BaseView<HotTopic> implements View.OnClickListener {

    private TextView tvNo;
    private TextView tvHotTopic;

    public SearchHotConversationView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_search_conversation_search_hot, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        tvNo = findView(R.id.tv_no);
        tvHotTopic = findView(R.id.tv_content);

        return super.createView();
    }

    @Override
    public void bindView(HotTopic data_){
        super.bindView(data_ != null ? data_ : new HotTopic());

        tvNo.setText("" + (position + 1));
        tvHotTopic.setText("#" + data.getTitle() + "#");
    }

    @Override
    public void onClick(View v) {

    }
}
