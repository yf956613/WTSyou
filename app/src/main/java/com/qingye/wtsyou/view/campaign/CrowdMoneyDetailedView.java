package com.qingye.wtsyou.view.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.CrowdMoneyDetailed;

import java.math.BigDecimal;

import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class CrowdMoneyDetailedView extends BaseView<CrowdMoneyDetailed> implements View.OnClickListener {

    private ImageView ivImg;
    private TextView tvName;
    private TextView tvDetailed;

    public CrowdMoneyDetailedView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_crowd_money_detailed, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        ivImg = findView(R.id.iv_fans_img);
        tvName = findView(R.id.tv_name);
        tvDetailed = findView(R.id.tv_detailed);

        return super.createView();
    }

    @Override
    public void bindView(CrowdMoneyDetailed data_){
        super.bindView(data_ != null ? data_ : new CrowdMoneyDetailed());

        //图片
        //图片
        String url = data.getPhoto();
        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .into(ivImg);
        } else {
            int defaultHead = R.mipmap.head;
            Glide.with(context)
                    .load(defaultHead)
                    .into(ivImg);
        }
        //名字
        tvName.setText(data.getUserName());
        //明细
        //已筹集
        BigDecimal joinBig = data.getTotal();
        double joinDou = joinBig.doubleValue();
        int joinInt = (int) joinDou;
        tvDetailed.setText("" + joinDou);

    }

    @Override
    public void onClick(View v) {

    }
}
