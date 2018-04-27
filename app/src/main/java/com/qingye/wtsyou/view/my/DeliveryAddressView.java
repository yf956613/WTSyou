package com.qingye.wtsyou.view.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.my.EditAddressActivity;
import com.qingye.wtsyou.modle.DeliveryAddress;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class DeliveryAddressView extends BaseView<DeliveryAddress> implements View.OnClickListener {

    private LinearLayout llEdit;

    public DeliveryAddressView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_delivery_address, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        llEdit = findViewById(R.id.ll_edit,this);

        return super.createView();
    }

    @Override
    public void bindView(DeliveryAddress data_){
        super.bindView(data_ != null ? data_ : new DeliveryAddress());

    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
        switch (v.getId()) {
            case R.id.ll_edit:
                toActivity(EditAddressActivity.createIntent(context));
                break;
            default:
                break;
        }
    }
}
