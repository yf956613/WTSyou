package com.qingye.wtsyou.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.fragment.home.FansMainActivityFragment;
import com.qingye.wtsyou.fragment.home.FansMainConversationFragment;
import com.qingye.wtsyou.fragment.home.FansMainIdolFragment;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class FansMainActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivMore;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @param id
     * @return
     */
    public static Intent createIntent(Context context, long id) {
        return new Intent(context,FansMainActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_main,this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        ivBack = findViewById(R.id.iv_left);
        ivMore = findViewById(R.id.iv_right);

        FansMainIdolFragment fansMainIdolFragment = new FansMainIdolFragment();
        FansMainConversationFragment fansMainConversationFragment = new FansMainConversationFragment();
        FansMainActivityFragment fansMainActivityFragment = new FansMainActivityFragment();
        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_focus_stars,fansMainIdolFragment);
        transaction.replace(R.id.list_focus_conversation,fansMainConversationFragment);
        transaction.replace(R.id.list_focus_campaign,fansMainActivityFragment);
        transaction.commit();
    }

    @Override
    public void initData() {
    }

    @Override
    public void initEvent() {

        ivBack.setOnClickListener(this);
        ivMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:

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

    }

}
