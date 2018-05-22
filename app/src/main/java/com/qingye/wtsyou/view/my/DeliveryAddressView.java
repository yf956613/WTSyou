package com.qingye.wtsyou.view.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.my.EditAddressActivity;
import com.qingye.wtsyou.modle.DeliveryAddress;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class DeliveryAddressView extends BaseView<DeliveryAddress> implements View.OnClickListener {

    private OnItemChildClickListener onItemChildClickListener;

    //定义一个监听接口，里面有方法
    public interface OnItemChildClickListener{
        void onEditClick(View view, int position);
        void onDeleteClick(View view, int position);
        void onDefaultClick(View view, int position);
    }

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    private TextView tvContactName;
    private TextView tvContactPhone;
    private TextView tvAddress;
    private CheckBox isDefault;
    private LinearLayout llEdit;
    private LinearLayout llDelete;

    public DeliveryAddressView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_delivery_address, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        tvContactName = findViewById(R.id.tv_contact_name);
        tvContactPhone = findViewById(R.id.tv_contact_phone);
        tvAddress = findViewById(R.id.tv_delivery_address);
        isDefault = findViewById(R.id.cbtn_default_address, this);
        llEdit = findViewById(R.id.ll_edit,this);
        llDelete = findViewById(R.id.ll_delete,this);

        return super.createView();
    }

    @Override
    public void bindView(DeliveryAddress data_){
        super.bindView(data_ != null ? data_ : new DeliveryAddress());

        tvContactName.setText(data.getName());
        tvContactPhone.setText(data.getMobile());
        //省
        String province = data.getPoi().getPcdt().getProvince();
        //市
        String city = data.getPoi().getPcdt().getCity();
        //区
        String district = data.getPoi().getPcdt().getDistrict();
        //街道
        String township = data.getPoi().getPcdt().getTownship();
        //poi
        //String poiString = data.getPoi().getPoiAddress() + data.getPoi().getPoiName();
        //详细
        String detail = data.getPoi().getAddress();

        tvAddress.setText(province + city + district + township + detail);
        if (data.getDefault()) {
            isDefault.setChecked(true);
        } else {
            isDefault.setChecked(false);
        }

    }

    @Override
    public void onClick(View v) {
        if (onItemChildClickListener != null) {

            switch (v.getId()) {
                case R.id.ll_edit:
                    onItemChildClickListener.onEditClick(v, position);
                    break;
                case R.id.ll_delete:
                    onItemChildClickListener.onDeleteClick(v, position);
                    break;
                case R.id.cbtn_default_address:
                    onItemChildClickListener.onDefaultClick(v, position);
                    break;
            }
        }
    }
}
