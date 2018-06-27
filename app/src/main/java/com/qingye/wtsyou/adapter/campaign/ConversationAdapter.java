package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.view.campaign.ConversationView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ConversationAdapter extends BaseAdapter<ChatingRoom,ConversationView> {

    public ConversationAdapter(Activity context) {
        super(context);
    }

    @Override
    public ConversationView createView(int position, ViewGroup parent) {
        return new ConversationView(context, parent);
    }

}
