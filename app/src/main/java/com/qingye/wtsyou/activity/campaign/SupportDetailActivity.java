package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.fragment.campaign.DetailedConversationFragment;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class SupportDetailActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivShare;
    private TextView tvHead;
    private ProgressBar joinProgressBar;//参与进度条
    private PopupWindow mPopupWindow;//百分比提示
    private View pop_wiew;
    private int popwidth,pophight;

    private int progressValue;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, long id) {
        return new Intent(context, SupportDetailActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_detail,this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        progressValue = 23;

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        ivBack = findViewById(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        ivShare = findViewById(R.id.iv_right);
        ivShare.setImageResource(R.mipmap.share_g);
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("应援详情");
        joinProgressBar = findViewById(R.id.join_progressbar);

        /*//弹出窗
        pop_wiew = LayoutInflater.from(this).inflate(R.layout.text_progress, null, false);
        mPopupWindow = new PopupWindow(pop_wiew, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        pop_wiew.post(new Runnable() {
            @Override
            public void run() {
                pop_wiew.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                popwidth = pop_wiew.getMeasuredWidth();
                pophight = pop_wiew.getMeasuredHeight();
            }
        });
        pop_wiew.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(false);

        joinProgressBar.post(new Runnable() {
            @Override
            public void run() {
                int width = joinProgressBar.getWidth();
                int height = joinProgressBar.getHeight();
                int[] location = new int[2];
                joinProgressBar.getLocationOnScreen(location);
                //获得进度条在屏幕中的x，y值
                int x = location[0];
                int y = location[1];
                //目前popwindow应该在的x值
                int poplocation = (int) (width * progressValue / 100 + x);
                mPopupWindow.showAtLocation(joinProgressBar, Gravity.NO_GRAVITY,
                        poplocation - popwidth / 2, y - pophight - height + 25);
            }
        });*/

        DetailedConversationFragment campaignDetailedConversationFragment = new DetailedConversationFragment();
        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_conversation,campaignDetailedConversationFragment);
        transaction.commit();
    }

    @Override
    public void initData() {
        joinProgressBar.setProgress(progressValue);
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
