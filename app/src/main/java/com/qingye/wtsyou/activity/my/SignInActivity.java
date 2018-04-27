package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingye.wtsyou.R;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class SignInActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private RelativeLayout rlContent;
    private ImageView ivLeft;
    private TextView tvHead;
    private TextView tvMon,tvTue,tvWed,tvThu,tvFri,tvSat,tvSun;
    private View lineTue,lineWed,lineThu,lineFri,lineSat,lineSun;
    private TextView tvSignIn;

    private int continuityDay,weekDay;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context,SignInActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin,this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        rlContent = findViewById(R.id.rl_content);

        ivLeft = findViewById(R.id.iv_left);
        ivLeft.setImageResource(R.mipmap.back_a);
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("签到");

        tvSignIn = findViewById(R.id.tv_signIn);

        //圆圈
        tvMon = findViewById(R.id.tv_monday);
        tvTue = findViewById(R.id.tv_tuesday);
        tvWed = findViewById(R.id.tv_wednesday);
        tvThu = findViewById(R.id.tv_thursday);
        tvFri = findViewById(R.id.tv_friday);
        tvSat = findViewById(R.id.tv_saturday);
        tvSun = findViewById(R.id.tv_sunday);

        //线
        lineTue = findViewById(R.id.tue_line);
        lineWed = findViewById(R.id.wed_line);
        lineThu = findViewById(R.id.thu_line);
        lineFri = findViewById(R.id.fri_line);
        lineSat = findViewById(R.id.sat_line);
        lineSun = findViewById(R.id.sun_line);

        changeColor(weekDay);
    }

    public void changeColor(int day) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        ivLeft.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.iv_left:
                finish();
                break;
            case R.id.tv_signIn:
                changeColor(weekDay + 1);
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
