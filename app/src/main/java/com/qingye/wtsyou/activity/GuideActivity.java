package com.qingye.wtsyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.qingye.wtsyou.R;

/**
 * Created by pm89 on 2018/6/23.
 */

public class GuideActivity extends AppCompatActivity{
    private Handler mHandler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    public void initView() {

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
