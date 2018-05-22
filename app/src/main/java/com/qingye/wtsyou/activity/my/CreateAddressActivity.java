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
import com.qingye.wtsyou.basemodel.EntityBase;
import com.qingye.wtsyou.basemodel.POI;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.widget.CustomDialog;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

public class CreateAddressActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivLeft;
    private TextView tvRight,tvHead;

    private EditText edtContactName;
    private EditText edtContactPhone;
    private EditText edtDetail;

    private POI selectedCity = null;

    private RelativeLayout llAddress;
    private TextView tvArea;

    private CustomDialog progressBar;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, CreateAddressActivity.class);
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

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        ivLeft = findViewById(R.id.iv_left);
        ivLeft.setImageResource(R.mipmap.back_a);
        tvRight = findViewById(R.id.tv_add_temp);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("保存");
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("新增收货地址");

        edtContactName = findViewById(R.id.edt_contact_name);
        edtContactPhone = findViewById(R.id.edt_contact_phone);
        edtDetail = findViewById(R.id.edt_detail);

        llAddress = findViewById(R.id.ll_address);
        tvArea = findViewById(R.id.tv_area);
    }

    @Override
    public void initData() {

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
                saveNewAddress();
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

    public void saveNewAddress() {
        String uuid = null;
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

        selectedCity.setAddress(detail);

        Boolean isDefault = null;

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
                            showShortToast(R.string.createAddressSuccess);

                            progressBarDismiss();

                            Intent intent = new Intent(
                                    BroadcastAction.ACTION_ADDRESS_REFRESH);
                            // 发送广播
                            sendBroadcast(intent);

                            finish();

                            progressBarDismiss();
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
    public void onDragBottom(boolean rightToLeft) {
        finish();
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
