package com.qingye.wtsyou.adapter.conversation;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.Conversation;
import com.qingye.wtsyou.view.conversation.ConversationHotView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ConversationHotAdapter extends BaseAdapter<Conversation,ConversationHotView> {

    public ConversationHotAdapter(Activity context) {
        super(context);
    }

    @Override
    public ConversationHotView createView(int position, ViewGroup parent) {
        return new ConversationHotView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
