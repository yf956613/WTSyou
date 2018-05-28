package com.qingye.wtsyou.adapter.my;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.CoinDetailed;
import com.qingye.wtsyou.view.my.CoinDetailedView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class CoinDetailedAdapter extends BaseAdapter<CoinDetailed,CoinDetailedView> {

    public CoinDetailedAdapter(Activity context) {
        super(context);
    }

    @Override
    public CoinDetailedView createView(int position, ViewGroup parent) {
        return new CoinDetailedView(context, parent);
    }

}
