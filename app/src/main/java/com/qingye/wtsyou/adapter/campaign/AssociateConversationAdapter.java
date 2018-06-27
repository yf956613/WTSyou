package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.view.campaign.AssociateConversationView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class AssociateConversationAdapter extends BaseAdapter<ChatingRoom,AssociateConversationView> {

    public AssociateConversationAdapter(Activity context) {
        super(context);
    }

    @Override
    public AssociateConversationView createView(int position, ViewGroup parent) {
        return new AssociateConversationView(context, parent);
    }

}
