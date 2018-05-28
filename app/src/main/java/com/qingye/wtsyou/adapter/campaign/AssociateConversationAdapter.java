package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.Conversation;
import com.qingye.wtsyou.view.campaign.AssociateConversationView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class AssociateConversationAdapter extends BaseAdapter<Conversation,AssociateConversationView> {

    public AssociateConversationAdapter(Activity context) {
        super(context);
    }

    @Override
    public AssociateConversationView createView(int position, ViewGroup parent) {
        return new AssociateConversationView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
