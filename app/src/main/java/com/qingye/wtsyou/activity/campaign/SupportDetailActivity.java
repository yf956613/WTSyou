package com.qingye.wtsyou.activity.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.fragment.campaign.DetailedConversationFragment;
import com.qingye.wtsyou.widget.ObservableScrollView;

import java.lang.reflect.Method;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class SupportDetailActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivShare;
    private TextView tvHead;
    private Button btnSupport;
    private Button btnSupportEnd;
    private ProgressBar joinProgressBar;//参与进度条
    private RelativeLayout tvProgress;
    private TextView tvProgressValue;
    private ImageView ivBubble;
    private int textwidth,texthight;

    private int progressValue;

    private LinearLayout llHead;
    private ObservableScrollView scrollView;
    private ImageView backImageView;
    private int imageHeight;
    private View line;

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

        progressValue = 72;

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
        btnSupport = findViewById(R.id.btn_support);
        btnSupportEnd = findViewById(R.id.btn_support_end);

        //进度条
        joinProgressBar = findViewById(R.id.join_progressbar);
        //气泡
        tvProgressValue = findViewById(R.id.text_value);
        ivBubble = findViewById(R.id.iv_bubble);
        //已结束
        //setProgressDrawable(joinProgressBar, R.drawable.progress_horizontal3);//进度条
        tvProgress = findViewById(R.id.progress_value);
        //ivBubble.setImageResource(R.mipmap.qipaohui);//气泡
        //按钮
        //btnSupportEnd.setText("应援结束");
        //btnSupportEnd.setBackgroundColor(getResources().getColor(R.color.gray_background3));

        //气泡位置
        setProgressText();

        llHead = findViewById(R.id.ll_head);
        backImageView = findViewById(R.id.iv_campaign_background_img);
        scrollView = findViewById(R.id.scrollview);
        line = findViewById(R.id.line);
        initListeners();

        DetailedConversationFragment campaignDetailedConversationFragment = new DetailedConversationFragment();
        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_conversation,campaignDetailedConversationFragment);
        transaction.commit();
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
                            line.setVisibility(View.GONE);
                        } else if (y > 0 && y <= imageHeight) {
                            line.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
    }

    @SuppressLint("NewApi")
    public static void setProgressDrawable(@NonNull ProgressBar bar, @DrawableRes int resId) {
        Drawable layerDrawable = bar.getResources().getDrawable(resId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable d = getMethod("tileify", bar, new Object[] { layerDrawable, false });
            bar.setProgressDrawable(d);
        } else {
            bar.setProgressDrawableTiled(layerDrawable);
        }
    }

    private static Drawable getMethod(String methodName, Object o, Object[] paras) {
        Drawable newDrawable = null;
        try {
            Class c[] = new Class[2];
            c[0] = Drawable.class;
            c[1] = boolean.class;
            Method method = ProgressBar.class.getDeclaredMethod(methodName, c);
            method.setAccessible(true);
            newDrawable = (Drawable) method.invoke(o, paras);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return newDrawable;
    }

    private void setProgressText() {
        //气泡提示
        tvProgress.post(new Runnable() {
            @Override
            public void run() {
                tvProgress.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                textwidth = tvProgress.getMeasuredWidth();
                texthight = tvProgress.getMeasuredHeight();
            }
        });

        //进度条
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
                //目前textview应该在的x值
                int textlocation = width * progressValue / 100 + x;
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvProgress.getLayoutParams();
                params.leftMargin = ( textlocation - textwidth / 2 );// 通过自定义坐标来放置你的控件
                tvProgress.setLayoutParams(params);
            }
        });

        tvProgressValue.setText(Integer.toString(progressValue));
    }

    @Override
    public void initData() {
        joinProgressBar.setProgress(progressValue);
    }

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
        btnSupport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                break;
            case R.id.btn_support:
                finish();
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
