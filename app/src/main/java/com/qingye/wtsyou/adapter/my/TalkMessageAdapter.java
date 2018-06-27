package com.qingye.wtsyou.adapter.my;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.Message;
import com.qingye.wtsyou.view.my.TalkMessageView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class TalkMessageAdapter extends BaseAdapter<Message,TalkMessageView> {

    public TalkMessageAdapter(Activity context) {
        super(context);
    }

    @Override
    public TalkMessageView createView(int position, ViewGroup parent) {
        return new TalkMessageView(context, parent);
    }

}
