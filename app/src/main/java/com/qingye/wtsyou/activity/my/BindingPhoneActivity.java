package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.EntityStringContent;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.widget.CountButton;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class BindingPhoneActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivLeft;
    private TextView tvRight,tvHead;
    private EditText edtPhone;
    private EditText edtCode;

    private String checkIndex;
    private CountButton btnGetVerifyCode;

    private CustomDialog progressBar;

    private HttpModel<EntityStringContent> mVerifyCodeHttpModel;
    private HttpModel<EntityBase> mBindHttpModel;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, BindingPhoneActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binging_phone,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //获取验证码
        mVerifyCodeHttpModel = new HttpModel<>(EntityStringContent.class);
        //绑定
        mBindHttpModel = new HttpModel<>(EntityBase.class);

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
        tvRight.setText("完成");
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("绑定手机");

        edtPhone = findView(R.id.edt_phone);
        edtCode = findView(R.id.edt_code);

        btnGetVerifyCode = (CountButton) findViewById(R.id.btn_getVerifyCode);
        btnGetVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mobile = edtPhone.getText().toString().trim();
                boolean checkPhone = checkPhone(mobile);
                //检查手机号
                if (checkPhone) {
                    getVerifyCode(mobile);
                }
            }
        });
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_add_temp:
                bind();
                break;
            default:
                break;
        }
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

    private boolean checkPhone(String mobile) {

        if(TextUtils.isEmpty(mobile)){
            showShortToast(R.string.editPhone);
            return false;
        } else {
            if (mobile.length() < 11) {
                showShortToast(R.string.checkPhone);
                return false;
            } else {
                return true;
            }
        }
    }

    private void getVerifyCode(String mobile) {

        String type = "mobile";
        String request = HttpRequest.postGetVerifyCode(type, mobile);
        mVerifyCodeHttpModel.post( request,URL_BASE + URLConstant.OTHERVERIFYCODE,1,this);
    }

    private void bind() {
        String type = "mobile";
        String code = edtCode.getText().toString().trim();
        String target = edtPhone.getText().toString().trim();

        String request = HttpRequest.postBind(checkIndex, code, type, target);
        mBindHttpModel.post( request, URL_BASE + URLConstant.BINDPHONE, 2, this);
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public void Success(String url, int RequestCode, EntityBase entityBase) {
        super.Success(url, RequestCode, entityBase);
        switch (RequestCode) {
            case 1:
                //成功
                showShortToast(R.string.sendSuccess);
                btnGetVerifyCode.start();

                checkIndex = mVerifyCodeHttpModel.getData().getContent();
                break;
            case 2:
                showShortToast(R.string.bindSuccess);
                finish();
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }
}
