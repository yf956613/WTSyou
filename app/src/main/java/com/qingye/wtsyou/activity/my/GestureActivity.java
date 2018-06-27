package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingye.wtsyou.R;
import com.syd.oden.gesturelock.view.GestureLockViewGroup;
import com.syd.oden.gesturelock.view.listener.GestureEventListener;
import com.syd.oden.gesturelock.view.listener.GesturePasswordSettingListener;
import com.syd.oden.gesturelock.view.listener.GestureUnmatchedExceedListener;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.GridAdapter;

public class GestureActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivLeft;
    private TextView tvHead;
    private TextView tvRight;

    private GestureLockViewGroup mGestureLockViewGroup;
    private boolean isReset = false;
    private TextView tvState;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, GestureActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);

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
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("手势密码");
        tvRight = findView(R.id.tv_add_temp);
        tvRight.setText("重置");
        tvRight.setVisibility(View.VISIBLE);

        tvState = findView(R.id.tv_state);
        mGestureLockViewGroup = findView(R.id.gesturelock);
        initGesture();
        setGestureWhenNoSet();
    }

    public void initGesture() {
        gestureEventListener();
        gesturePasswordSettingListener();
        //gestureRetryLimitListener();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    private void gestureEventListener() {
        mGestureLockViewGroup.setGestureEventListener(new GestureEventListener() {
            @Override
            public void onGestureEvent(boolean matched) {

                if (!matched) {
                    tvState.setTextColor(Color.RED);
                    tvState.setText("手势密码错误");
                } else {
                    if (isReset) {
                        isReset = false;
                        Toast.makeText(GestureActivity.this, "清除成功!", Toast.LENGTH_SHORT).show();
                        resetGesturePattern();
                    } else {
                        tvState.setTextColor(getResources().getColor(R.color.black_text1));
                        tvState.setText("手势密码正确");
                    }
                }
            }
        });
    }

    private void gesturePasswordSettingListener() {
        mGestureLockViewGroup.setGesturePasswordSettingListener(new GesturePasswordSettingListener() {
            @Override
            public boolean onFirstInputComplete(int len) {
                if (len > 3) {
                    tvState.setTextColor(getResources().getColor(R.color.black_text1));
                    tvState.setText("再次绘制手势密码");
                    return true;
                } else {
                    tvState.setTextColor(Color.RED);
                    tvState.setText("最少连接4个点，请重新输入!");
                    return false;
                }
            }

            @Override
            public void onSuccess() {
                tvState.setTextColor(getResources().getColor(R.color.black_text1));
                Toast.makeText(GestureActivity.this, "密码设置成功!", Toast.LENGTH_SHORT).show();
                tvState.setText("请输入手势密码解锁!");
            }

            @Override
            public void onFail() {
                tvState.setTextColor(Color.RED);
                tvState.setText("与上一次绘制不一致，请重新绘制");
            }
        });
    }

    /*private void gestureRetryLimitListener() {
        mGestureLockViewGroup.setGestureUnmatchedExceedListener(3, new GestureUnmatchedExceedListener() {
            @Override
            public void onUnmatchedExceedBoundary() {
                tvState.setTextColor(Color.RED);
                tvState.setText("错误次数过多，请稍后再试!");
            }
        });
    }*/

    private void setGestureWhenNoSet() {
        if (!mGestureLockViewGroup.isSetPassword()){
            tvState.setTextColor(getResources().getColor(R.color.black_text1));
            tvState.setText("绘制手势密码");
        }
    }

    private void resetGesturePattern() {
        mGestureLockViewGroup.removePassword();
        setGestureWhenNoSet();
        mGestureLockViewGroup.resetView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_add_temp:
                isReset = true;
                tvState.setTextColor(getResources().getColor(R.color.black_text1));
                tvState.setText("请输入原手势密码");
                mGestureLockViewGroup.resetView();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
