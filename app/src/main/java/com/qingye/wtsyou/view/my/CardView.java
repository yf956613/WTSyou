package com.qingye.wtsyou.view.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.Card;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class CardView extends BaseView<Card> implements View.OnClickListener {

    private ImageView ivMore;
    private ImageView ivLess;
    private LinearLayout llDetailed;

    public CardView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_card, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        ivMore = findViewById(R.id.iv_more,this);
        ivLess = findViewById(R.id.iv_less,this);
        llDetailed = findViewById(R.id.ll_detailed);

        return super.createView();
    }

    @Override
    public void bindView(Card data_){
        super.bindView(data_ != null ? data_ : new Card());

    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
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
