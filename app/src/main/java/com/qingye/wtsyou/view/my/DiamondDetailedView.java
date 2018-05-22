package com.qingye.wtsyou.view.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.DiamondDetailed;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class DiamondDetailedView extends BaseView<DiamondDetailed> implements View.OnClickListener {

    private TextView tvType;
    private TextView tvTime;
    private TextView tvValue;

    public DiamondDetailedView(Activity context, ViewGroup parent) {
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
    public void bindView(DiamondDetailed data_){
        super.bindView(data_ != null ? data_ : new DiamondDetailed());

        //充值
        if (data.getBizType().equals("sign")) {
            tvType.setText("充值");
        }
        //时间
        tvTime.setText(data.getOccurTime());
        //数量
        tvValue.setText("+ " + Integer.toString(data.getDiamondCount()));
        //数量
        if (data.getDiamondCount() < 0) {
            //数量
            tvValue.setText(Integer.toString(data.getDiamondCount()));
            tvValue.setTextColor(getColor(R.color.black_text1));
        } else {
            //数量
            tvValue.setText("+" + Integer.toString(data.getDiamondCount()));
            tvValue.setTextColor(getColor(R.color.orange_text2));
        }
    }

    @Override
    public void onClick(View v) {

    }
}
