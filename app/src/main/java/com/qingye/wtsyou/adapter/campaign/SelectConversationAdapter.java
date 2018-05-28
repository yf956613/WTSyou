package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.Conversation;
import com.qingye.wtsyou.view.campaign.SelectConversationView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SelectConversationAdapter extends BaseAdapter<Conversation,SelectConversationView> {

    public SelectConversationAdapter(Activity context) {
        super(context);
    }

    @Override
    public SelectConversationView createView(int position, ViewGroup parent) {
        return new SelectConversationView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
