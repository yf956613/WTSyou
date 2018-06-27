package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.utils.CacheUtil;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.widget.CircleProgress;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.StringUtil;

public class CleanActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivLeft;
    private TextView tvHead;
    private Button btnClean;
    private TextView tvCache;

    private String cache;

    private CircleProgress mcircleProgressBar;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, String cache) {
        return new Intent(context,CleanActivity.class).putExtra(Constant.CACHE, cache);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean,this);

        intent = getIntent();
        cache = intent.getStringExtra(Constant.CACHE);

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
        tvHead.setText("清除缓存");

        mcircleProgressBar = findView(R.id.join_progressbar);
        mcircleProgressBar.setPercentColor(R.color.black_text1);
        btnClean = findView(R.id.btn_clean);

        tvCache = findView(R.id.tvProgress);
    }

    @Override
    public void initData() {
        mcircleProgressBar.setMaxProgress(100);
        mcircleProgressBar.setSymbol(StringUtil.returnResultMultiple(cache));
        mcircleProgressBar.setPercentColor(R.color.white);
        tvCache.setText(cache);

        mcircleProgressBar.setMaxProgress(100);
        mcircleProgressBar.updateProgress(100,700);
    }

    @Override
    public void initEvent() {
        ivLeft.setOnClickListener(this);
        btnClean.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_clean:
                CacheUtil.clearAllCache(context);
                tvCache.setText(0 + StringUtil.returnResultMultiple(cache));
                mcircleProgressBar.updateProgress(0,700);
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
