package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.fragment.activity.CampaignDetailedConversationFragment;
import com.qingye.wtsyou.widget.CircleProgressBar;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class VoteDetailActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivShare;

    private CircleProgressBar mcircleProgressBar;
    private int currentProgress=0;
    private int bar4CurrentPro=0;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, long id) {
        return new Intent(context, VoteDetailActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_detail,this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        ivBack = findViewById(R.id.iv_left);
        ivShare = findViewById(R.id.iv_right);
        mcircleProgressBar = findViewById(R.id.join_progressbar);

        CampaignDetailedConversationFragment campaignDetailedConversationFragment = new CampaignDetailedConversationFragment();
        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_conversation,campaignDetailedConversationFragment);
        transaction.commit();
    }

    @Override
    public void initData() {
        new Thread() {
            public void run() {
                mcircleProgressBar.setMaxProgress(100);
                bar4CurrentPro = 78;

                while (currentProgress <= 100) {
                    if (bar4CurrentPro <= 100) {
                        mcircleProgressBar.setCurrentProgress(bar4CurrentPro);
                    }

                    //bar4CurrentPro += 10;
                    //currentProgress += 5;
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }


            }
        }.start();
    }

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
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
