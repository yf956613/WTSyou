package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.fragment.campaign.DetailedConversationFragment;
import com.qingye.wtsyou.widget.CircleProgress;
import com.qingye.wtsyou.widget.CircleProgressBar;
import com.qingye.wtsyou.widget.ObservableScrollView;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class CrowdDetailedActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivShare;
    private Button btnCrowd,btnCrowdEnd;
    private LinearLayout llParticipate;
    private LinearLayout llConversation;
    private LinearLayout llDetailed;

    private CircleProgress mcircleProgressBar;
    private int progress = 0;
    private int currentProgress = 0;
    private int bar4CurrentPro = 0;

    private LinearLayout llHead;
    private ObservableScrollView scrollView;
    private ImageView backImageView;
    private int imageHeight;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context,long id) {
        return new Intent(context, CrowdDetailedActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowd_detailed,this);

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
        btnCrowd = findViewById(R.id.btn_crowd);
        btnCrowdEnd = findViewById(R.id.btn_crowd_end);

        llParticipate = findViewById(R.id.ll_participate);
        llConversation = findViewById(R.id.ll_conversation);
        llDetailed = findViewById(R.id.ll_detailed);

        mcircleProgressBar = findViewById(R.id.join_progressbar);
        mcircleProgressBar.setPercentColor(R.color.black_text1);

        llHead = findViewById(R.id.ll_head);
        backImageView = findViewById(R.id.iv_campaign_background_img);
        scrollView = findViewById(R.id.scrollview);
        initListeners();

        /*DetailedConversationFragment campaignDetailedConversationFragment = new DetailedConversationFragment();
        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_conversation,campaignDetailedConversationFragment);
        transaction.commit();*/
    }

    private void initListeners() {
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = backImageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                backImageView.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = backImageView.getHeight();
                scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
                    @Override
                    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                        // TODO Auto-generated method stub
                        if (y <= 0) {
                            llHead.setBackgroundColor(Color.parseColor("#00ffffff"));
                            ivBack.setImageResource(R.mipmap.back_l);
                            ivShare.setImageResource(R.mipmap.share_w);
                        } else if (y > 0 && y <= imageHeight) {
                            llHead.setBackgroundColor(Color.parseColor("#ddffffff"));
                            ivBack.setImageResource(R.mipmap.back_a);
                            ivShare.setImageResource(R.mipmap.share_g);
                        }
                    }
                });

            }
        });


    }

    @Override
    public void initData() {
        mcircleProgressBar.setMaxProgress(100);
        currentProgress = 78;

        mcircleProgressBar.updateProgress(currentProgress,700);
    }

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
        btnCrowd.setOnClickListener(this);
        llParticipate.setOnClickListener(this);
        llConversation.setOnClickListener(this);
        llDetailed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                break;
            case R.id.btn_crowd:
                finish();
                break;
            case R.id.ll_participate:
                toActivity(CrowdFansActivity.createIntent(context));
                break;
            case R.id.ll_conversation:
                toActivity(CrowdConversationActivity.createIntent(context));
                break;
            case R.id.ll_detailed:
                toActivity(CrowdMoneyDetailedActivity.createIntent(context));
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
