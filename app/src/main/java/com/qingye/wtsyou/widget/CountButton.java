package com.qingye.wtsyou.widget;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import android.widget.Button;

import com.qingye.wtsyou.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pm89 on 2018/5/7.
 */

public class CountButton extends Button implements View.OnClickListener {

    private Context context;

    /*
     倒计时时长,默认计时时间
     */
    private long defaultTime = 6 * 1000;
    private long time = defaultTime;

    /*
    开始执行计时的类,可以每间隔一秒执行任务
     */
    private Timer timer;

    /*
    执行的任务
     */
    private TimerTask task;

    /*
    默认文案
     */
    private String defaultText = "获取验证码";
    /*
    计时完成之后显示的文案
     */
    private String finishText = "重新获取";

    private boolean isConnect = false;

    /*
    点击事件
     */
    private OnClickListener onClickListener;

    public CountButton(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CountButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public CountButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }


    private void initView() {

        if (!TextUtils.isEmpty(getText())) {
            defaultText = getText().toString().trim();
        }
        this.setText(defaultText);
        setOnClickListener(this);
    }

    /*
    更新显示文案
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CountButton.this.setText("重新获取(" + time / 1000 + "s)");
            CountButton.this.setTextColor(Color.parseColor("#9c9c9c"));
            time -= 1000;
            if (time < 0) {
                CountButton.this.setEnabled(true);
                CountButton.this.setText(finishText);
                CountButton.this.setTextColor(Color.parseColor("#fb8063"));
                clearTimer();
                time = defaultTime;
            }
        }
    };

    /*
    清除倒计时
     */
    private void clearTimer() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /*
    初始化时间
     */
    private void initTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
    }


/*
当activity或者fragment消亡的时候清除倒计时
 */

    @Override
    protected void onDetachedFromWindow() {
        clearTimer();
        super.onDetachedFromWindow();
    }


    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }


    public void setFinishText(String finishText) {
        this.finishText = finishText;
    }


    public void setDefaultTime(long defaultTime) {
        this.defaultTime = defaultTime;
    }

    @Override
    public void onClick(View view) {

        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    public void start() {
        initTimer();
        this.setText(time / 1000 + " 秒");
        this.setEnabled(false);
        timer.schedule(task, 0, 1000);
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {//提供外部访问方法
        if (onClickListener instanceof CountButton) {
            super.setOnClickListener(onClickListener);
        }else{
            this.onClickListener = onClickListener;
        }
    }

}

