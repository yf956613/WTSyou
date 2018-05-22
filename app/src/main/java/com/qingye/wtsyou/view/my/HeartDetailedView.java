package com.qingye.wtsyou.view.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.HeartDetailed;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HeartDetailedView extends BaseView<HeartDetailed> implements View.OnClickListener {

    private TextView tvType;
    private TextView tvTime;
    private TextView tvValue;

    public HeartDetailedView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_detailed, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        tvType = findViewById(R.id.tv_type);
        tvTime = findViewById(R.id.tv_time);
        tvValue = findViewById(R.id.tv_value);

        return super.createView();
    }

    @Override
    public void bindView(HeartDetailed data_){
        super.bindView(data_ != null ? data_ : new HeartDetailed());

        //签到
        if (data.getBizType().equals("sign")) {
            tvType.setText("签到");
        }
        //打榜
        if (data.getBizType().equals("hit")) {
            tvType.setText("打榜");
        }
        if (data.getLoveValue() < 0) {
            //数量
            tvValue.setText(Integer.toString(data.getLoveValue()));
            tvValue.setTextColor(getColor(R.color.black_text1));
        } else {
            //数量
            tvValue.setText("+" + Integer.toString(data.getLoveValue()));
            tvValue.setTextColor(getColor(R.color.orange_text2));
        }
        //时间
        tvTime.setText(data.getOccurTime());


    }

    @Override
    public void onClick(View v) {

    }
}
