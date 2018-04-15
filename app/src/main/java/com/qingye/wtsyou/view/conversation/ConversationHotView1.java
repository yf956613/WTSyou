package com.qingye.wtsyou.view.conversation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.Conversation;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ConversationHotView1 extends BaseView<Conversation> implements View.OnClickListener {

    public ConversationHotView1(Activity context, ViewGroup parent) {
        super(context, R.layout.list_conversation_hot1, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        return super.createView();
    }

    @Override
    public void bindView(Conversation data_){
        super.bindView(data_ != null ? data_ : new Conversation());

    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
