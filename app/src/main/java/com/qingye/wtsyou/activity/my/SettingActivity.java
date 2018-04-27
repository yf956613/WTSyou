package com.qingye.wtsyou.activity.my;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingye.wtsyou.R;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class SettingActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivLeft;
    private TextView tvHead;
    private RelativeLayout rlPhone;
    private RelativeLayout rlEmail;
    private RelativeLayout rlIdentify;
    private RelativeLayout rlClean;
    private RelativeLayout rlFeedBack;
    private RelativeLayout rlContact;
    private RelativeLayout rlAbout;

    private String mobile;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting, this);

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
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("设置");

        rlPhone = findViewById(R.id.rlPhone);
        rlEmail = findViewById(R.id.rlEmail);
        rlIdentify = findViewById(R.id.rlIdentify);
        rlClean = findViewById(R.id.rlClean);
        rlFeedBack = findViewById(R.id.rlFeedBack);
        rlContact = findViewById(R.id.rlContact);
        rlAbout = findViewById(R.id.rlAbout);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        ivLeft.setOnClickListener(this);
        rlPhone.setOnClickListener(this);
        rlEmail.setOnClickListener(this);
        rlIdentify.setOnClickListener(this);
        rlClean.setOnClickListener(this);
        rlFeedBack.setOnClickListener(this);
        rlContact.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.rlPhone:
                toActivity(ChangePhoneActivity.createIntent(context));
                break;
            case R.id.rlEmail:
                toActivity(BindingEmailActivity.createIntent(context));
                break;
            case R.id.rlIdentify:
                toActivity(IdentifyActivity.createIntent(context));
                break;
            case R.id.rlClean:
                toActivity(CleanActivity.createIntent(context));
                break;
            case R.id.rlFeedBack:
                toActivity(QuestionActivity.createIntent(context));
                break;
            case R.id.rlContact:
                mobile = "10086";
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.CALL_PHONE},1);
                }else {
                    call();
                }
                break;
            case R.id.rlAbout:
                toActivity(AboutActivity.createIntent(context));
                break;
            default:
                break;
        }
    }

    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mobile));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int [] granResults) {
        switch (requestCode) {
            case 1:
                if (granResults.length > 0 && granResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    showShortToast("您没有打开拨号权限");
                }
                break;
            default:
        }
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
