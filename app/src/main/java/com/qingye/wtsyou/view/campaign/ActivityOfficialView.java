package com.qingye.wtsyou.view.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.Campaign;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ActivityOfficialView extends BaseView<Campaign> implements View.OnClickListener {

    private RelativeLayout rlTag1,rlTag2;//1、2页标志

    public ActivityOfficialView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_activity_official, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        rlTag1 = findView(R.id.rl_tag1);
        rlTag2 = findView(R.id.rl_tag2);

        return super.createView();
    }

    @Override
    public void bindView(Campaign data_){
        super.bindView(data_ != null ? data_ : new Campaign());
        if(position == 1) {
            rlTag1.setVisibility(View.INVISIBLE);
            rlTag2.setVisibility(View.VISIBLE);
        } else {
            rlTag1.setVisibility(View.VISIBLE);
            rlTag2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
