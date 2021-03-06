package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.gaode.activity.GaoDeAddressSelectActivity;
import zuo.biao.library.model.EntityBase;
import com.qingye.wtsyou.basemodel.POI;
import com.qingye.wtsyou.model.DeliveryAddress;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import zuo.biao.library.widget.CustomDialog;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

public class EditAddressActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivLeft;
    private TextView tvRight,tvHead;

    private EditText edtContactName;
    private EditText edtContactPhone;
    private EditText edtDetail;

    private POI selectedCity = null;

    private RelativeLayout llAddress;
    private TextView tvArea;

    private CustomDialog progressBar;

    private DeliveryAddress deliveryAddress;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, DeliveryAddress deliveryAddress) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DELIVERYADDRESS, deliveryAddress);//放进数据流中
        return new Intent(context, EditAddressActivity.class).putExtras(bundle);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_address,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        intent = getIntent();
        deliveryAddress = (DeliveryAddress) intent.getSerializableExtra(Constant.DELIVERYADDRESS);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        ivLeft = findView(R.id.iv_left);
        ivLeft.setImageResource(R.mipmap.back_a);
        tvRight = findView(R.id.tv_add_temp);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("保存");
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("修改收货地址");

        //联系人名称
        edtContactName = findView(R.id.edt_contact_name);
        //联系电话
        edtContactPhone = findView(R.id.edt_contact_phone);
        //详细地址
        edtDetail = findView(R.id.edt_detail);

        llAddress = findView(R.id.ll_address);
        //所在区域
        tvArea = findView(R.id.tv_area);
    }

    @Override
    public void initData() {
        //联系人名称
        edtContactName.setText(deliveryAddress.getName());
        //联系电话
        edtContactPhone.setText(deliveryAddress.getMobile());
        //省
        String province = deliveryAddress.getPoi().getPcdt().getProvince();
        //市
        String city = deliveryAddress.getPoi().getPcdt().getCity();
        //区
        String district = deliveryAddress.getPoi().getPcdt().getDistrict();
        //街道
        String township = deliveryAddress.getPoi().getPcdt().getTownship();
        //所在区域
        tvArea.setText(province + city + district + township);
        //详细地址
        edtDetail.setText(deliveryAddress.getPoi().getAddress());

        selectedCity = deliveryAddress.getPoi();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }

            progressBar = null;
        }
    }

    private void setProgressBar() {
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void progressBarDismiss() {
        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
                progressBar.cancel();
            }
        }
    }

    @Override
    public void initEvent() {
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        llAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_add_temp:
                editAddress();
                break;
            case R.id.ll_address:
                toActivity(GaoDeAddressSelectActivity.createIntent(context),REQUEST_TO_SELECT_AREA);
                break;
            default:
                break;
        }
    }

    private static final int REQUEST_TO_SELECT_AREA = 1;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TO_SELECT_AREA:
                if (data != null) {
                    selectedCity = (POI) data.getExtras().getSerializable(Constant.SELECTED_ADDRESS);

                    //省
                    String province = selectedCity.getPcdt().getProvince();
                    //市
                    String city = selectedCity.getPcdt().getCity();
                    //区
                    String district = selectedCity.getPcdt().getDistrict();
                    //街道
                    String township = selectedCity.getPcdt().getTownship();

                    tvArea.setText(province + city + district + township);
                }
                break;
            default:
                break;
        }
    }

    public void editAddress() {
        String uuid = deliveryAddress.getUuid();
        String contactName = edtContactName.getText().toString().trim();
        String contactPhone = edtContactPhone.getText().toString().trim();
        String area = tvArea.getText().toString().trim();
        String detail = edtDetail.getText().toString().trim();

        if (TextUtils.isEmpty(contactName)){
            showShortToast(R.string.editContactName);
            return;
        }
        if (TextUtils.isEmpty(contactPhone)){
            showShortToast(R.string.editPhone);
            return;
        } else {
            if (contactPhone.length() < 11) {
                showShortToast(R.string.checkPhone);
                return;
            }
        }
        if (TextUtils.isEmpty(area)) {
            showShortToast(R.string.editPOIAddress);
            return;
        } else {
            selectedCity.setAddress(detail);
        }
        if (TextUtils.isEmpty(detail)){
            showShortToast(R.string.editDetailAddress);
            return;
        }

        Boolean isDefault = deliveryAddress.getDefault();

        if (NetUtil.checkNetwork(context)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.postSaveAddress(0, uuid, contactName, contactPhone, selectedCity, isDefault, new OnHttpResponseListener() {

                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功
                            showShortToast(R.string.editAddressSuccess);
                            progressBarDismiss();

                            Intent intent = new Intent(
                                    BroadcastAction.ACTION_ADDRESS_REFRESH);
                            // 发送广播
                            sendBroadcast(intent);

                            progressBarDismiss();

                            finish();

                        }else{//显示失败信息
                            if (entityBase.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityBase.getMessage());
                            }

                            progressBarDismiss();
                        }
                    }else{
                        showShortToast(R.string.noReturn);

                        progressBarDismiss();
                    }
                }
            });
        } else {
            showShortToast(R.string.checkNetwork);
        }


        finish();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }

        return super.onKeyUp(keyCode, event);
    }
}
