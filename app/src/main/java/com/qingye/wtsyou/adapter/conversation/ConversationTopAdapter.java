package com.qingye.wtsyou.adapter.conversation;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Conversation;
import com.qingye.wtsyou.view.conversation.ConversationTopView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ConversationTopAdapter extends BaseAdapter<Conversation,ConversationTopView> {

    public ConversationTopAdapter(Activity context) {
        super(context);
    }

    @Override
    public ConversationTopView createView(int position, ViewGroup parent) {
        return new ConversationTopView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
