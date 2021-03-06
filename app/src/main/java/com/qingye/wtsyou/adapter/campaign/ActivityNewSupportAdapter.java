package com.qingye.wtsyou.adapter.campaign;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.Supports;
import com.qingye.wtsyou.view.campaign.ActivityNewSupportView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ActivityNewSupportAdapter extends BaseAdapter<Supports,ActivityNewSupportView> {

    public ActivityNewSupportAdapter(Activity context) {
        super(context);
    }

    @Override
    public ActivityNewSupportView createView(int position, ViewGroup parent) {
        return new ActivityNewSupportView(context, parent);
    }

}
