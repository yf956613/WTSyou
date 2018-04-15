package com.qingye.wtsyou.adapter.my;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.modle.Card;
import com.qingye.wtsyou.view.my.CardView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class CardAdapter extends BaseAdapter<Card,CardView> {

    public CardAdapter(Activity context) {
        super(context);
    }

    @Override
    public CardView createView(int position, ViewGroup parent) {
        return new CardView(context, parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
