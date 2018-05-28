package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.CrowdFans;
import com.qingye.wtsyou.model.Fans;
import com.qingye.wtsyou.view.campaign.CrowdFansView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class CrowdFansAdapter extends BaseAdapter<CrowdFans,CrowdFansView> {

    public CrowdFansAdapter(Activity context) {
        super(context);
    }

    @Override
    public CrowdFansView createView(int position, ViewGroup parent) {
        return new CrowdFansView(context, parent);
    }

}
