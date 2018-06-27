package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.model.ChatingRoomItem;
import com.qingye.wtsyou.view.campaign.SelectConversationView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SelectConversationAdapter extends BaseAdapter<ChatingRoomItem,SelectConversationView> {

    public SelectConversationAdapter(Activity context) {
        super(context);
    }

    @Override
    public SelectConversationView createView(int position, ViewGroup parent) {
        return new SelectConversationView(context, parent);
    }

}
