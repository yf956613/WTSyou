package com.qingye.wtsyou.view.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.Message;
import com.qingye.wtsyou.utils.DateUtil;

import zuo.biao.library.base.BaseView;

import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_2;

/**
 * Created by pm89 on 2018/3/6.
 */

public class MessageView extends BaseView<Message> implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvContent;

    public MessageView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_message, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        tvTitle = findView(R.id.tv_title);
        tvTime = findView(R.id.tv_time);
        tvContent = findView(R.id.tv_message);

        return super.createView();
    }

    @Override
    public void bindView(Message data_){
        super.bindView(data_ != null ? data_ : new Message());

        tvTitle.setText(data.getTitle());
        tvTime.setText(DateUtil.showDate2(DateUtil.strToDate(data.getCreated(),DATE_PATTERN_2),true));
        tvContent.setText(data.getDescribe());
    }

    @Override
    public void onClick(View v) {

    }
}
