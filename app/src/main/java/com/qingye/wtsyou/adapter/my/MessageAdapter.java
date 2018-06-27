package com.qingye.wtsyou.adapter.my;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.Message;
import com.qingye.wtsyou.view.my.MessageView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class MessageAdapter extends BaseAdapter<Message,MessageView> {

    public MessageAdapter(Activity context) {
        super(context);
    }

    @Override
    public MessageView createView(int position, ViewGroup parent) {
        return new MessageView(context, parent);
    }

}
