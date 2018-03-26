package com.qingye.wtsyou.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qingye.wtsyou.activity.MainTabActivity;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

import com.qingye.wtsyou.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private Button btnLogin;
    private TextView tvModifyPwd,tvRegister;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context,LoginActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login,this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        btnLogin = findViewById(R.id.btn_login);
        tvModifyPwd = findViewById(R.id.tv_modify_pwd);
        tvRegister =  findViewById(R.id.tv_register);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        btnLogin.setOnClickListener(this);
        tvModifyPwd.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                toActivity(MainTabActivity.createIntent(context));
                break;
            case R.id.tv_modify_pwd:
                toActivity(ModifyPasswordActivity.createIntent(context));
                break;
            case R.id.tv_register:
                toActivity(RegisterActivity.createIntent(context));
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onDragBottom(boolean rightToLeft) {

    }
}
