package com.qingye.wtsyou.view.conversation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.ChatingRoom;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ConversationHotView1 extends BaseView<ChatingRoom> implements View.OnClickListener {

    public ConversationHotView1(Activity context, ViewGroup parent) {
        super(context, R.layout.list_conversation_hot1, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        return super.createView();
    }

    @Override
    public void bindView(ChatingRoom data_){
        super.bindView(data_ != null ? data_ : new ChatingRoom());

    }

    @Override
    public void onClick(View v) {

    }
}
