package com.qingye.wtsyou.view.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.Card;
import com.qingye.wtsyou.utils.DateUtil;

import zuo.biao.library.base.BaseView;

import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_2;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_5;

/**
 * Created by pm89 on 2018/3/6.
 */

public class CardView extends BaseView<Card> implements View.OnClickListener {

    private TextView tvName;
    private TextView tvTime;
    private TextView tvDiscount;
    private TextView tvMinAmount;
    private ImageView ivMore;
    private ImageView ivLess;
    private LinearLayout llDetailed;
    private ImageView ivUsedImg;
    private ImageView ivUsed;
    private TextView tvDetailed;

    public CardView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_card, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        tvName = findViewById(R.id.tv_name);
        tvTime = findViewById(R.id.tv_time);
        tvDiscount = findViewById(R.id.tv_discount);
        tvMinAmount = findViewById(R.id.tv_minAmount);
        ivMore = findViewById(R.id.iv_more,this);
        ivLess = findViewById(R.id.iv_less,this);
        llDetailed = findViewById(R.id.ll_detailed);
        ivUsedImg = findViewById(R.id.iv_used_img);
        ivUsed = findViewById(R.id.iv_used);
        tvDetailed = findViewById(R.id.tv_detailed);

        return super.createView();
    }

    @Override
    public void bindView(Card data_){
        super.bindView(data_ != null ? data_ : new Card());

        tvName.setText(data.getTitle());
        tvTime.setText(DateUtil.resverDate(data.getDeadline(),DATE_PATTERN_2,DATE_PATTERN_5));
        tvDiscount.setText("" + data.getDiscountAmount());
        tvMinAmount.setText("" + data.getMinAmount());
        if (data.getCouponState().equals("used") || data.getCouponState().equals("overdue")) {
            ivUsedImg.setImageResource(R.mipmap.combinedshape_b);
            ivUsed.setVisibility(View.VISIBLE);
        } else {
            ivUsedImg.setImageResource(R.mipmap.combinedshape);
            ivUsed.setVisibility(View.GONE);
        }
        if (data.getRestriction() != null) {
            tvDetailed.setText(data.getRestriction());
            ivMore.setEnabled(true);
            ivLess.setEnabled(true);
        } else {
            ivMore.setEnabled(false);
            ivLess.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_more:
                ivMore.setVisibility(View.GONE);
                ivLess.setVisibility(View.VISIBLE);
                llDetailed.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_less:
                ivMore.setVisibility(View.VISIBLE);
                ivLess.setVisibility(View.GONE);
                llDetailed.setVisibility(View.GONE);
                break;
        }
    }
}
