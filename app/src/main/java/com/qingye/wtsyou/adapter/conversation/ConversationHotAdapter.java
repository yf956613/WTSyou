package com.qingye.wtsyou.adapter.conversation;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.view.conversation.ConversationHotView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ConversationHotAdapter extends BaseAdapter<ChatingRoom,ConversationHotView> {

    public ConversationHotAdapter(Activity context) {
        super(context);
    }

    @Override
    public ConversationHotView createView(int position, ViewGroup parent) {
        return new ConversationHotView(context, parent);
    }

}
