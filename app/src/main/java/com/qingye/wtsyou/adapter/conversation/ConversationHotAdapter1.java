package com.qingye.wtsyou.adapter.conversation;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.view.conversation.ConversationHotView1;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ConversationHotAdapter1 extends BaseAdapter<ChatingRoom,ConversationHotView1> {

    public ConversationHotAdapter1(Activity context) {
        super(context);
    }

    @Override
    public ConversationHotView1 createView(int position, ViewGroup parent) {
        return new ConversationHotView1(context, parent);
    }

}
