package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.CrowdMoneyDetailed;
import com.qingye.wtsyou.view.campaign.CrowdMoneyDetailedView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class CrowdMoneyDetailedAdapter extends BaseAdapter<CrowdMoneyDetailed,CrowdMoneyDetailedView> {

    public CrowdMoneyDetailedAdapter(Activity context) {
        super(context);
    }

    @Override
    public CrowdMoneyDetailedView createView(int position, ViewGroup parent) {
        return new CrowdMoneyDetailedView(context, parent);
    }

}
