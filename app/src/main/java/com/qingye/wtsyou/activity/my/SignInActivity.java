package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import zuo.biao.library.model.EntityBase;
import com.qingye.wtsyou.model.EntitySignRecord;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import zuo.biao.library.widget.CustomDialog;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

public class SignInActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivLeft;
    private TextView tvHead;
    private TextView tvMon,tvTue,tvWed,tvThu,tvFri,tvSat,tvSun;
    private View lineTue,lineWed,lineThu,lineFri,lineSat,lineSun;
    private TextView tvSignIn;
    private TextView tvContinuityDay,tvWeekDay;

    private EntitySignRecord entitySignRecord;
    private int continuityDay,weekDay;

    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

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

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        getSignRecord();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        swipeRefresh = findViewById(R.id.swipe_refresh_widget);

        ivLeft = findViewById(R.id.iv_left);
        ivLeft.setImageResource(R.mipmap.back_a);
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("签到");

        tvContinuityDay = findViewById(R.id.tv_continuity_day);
        tvWeekDay = findViewById(R.id.tv_week);

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
    }

    public void onResume() {

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }

            progressBar = null;
        }
    }

    private void setProgressBar() {
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void progressBarDismiss() {
        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
                progressBar.cancel();
            }
        }
    }

    //刷新
    private void initHrvsr(){
        //设置刷新时动画的颜色，可以设置4个
        swipeRefresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefresh.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("swipeRefresh", "invoke onRefresh...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSignRecord();
                        showShortToast(R.string.getSuccess);
                        swipeRefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }

    @Override
    public void initData() {
        tvContinuityDay.setText(Integer.toString(entitySignRecord.getContent().getContinuousCount()));
        tvWeekDay.setText(Integer.toString(entitySignRecord.getContent().getContinuousCount()));

        continuityDay = entitySignRecord.getContent().getContinuousCount();
        weekDay = entitySignRecord.getContent().getSignCount();

        changeColor(weekDay);
    }

    public void changeColor(int day) {
        switch (day) {
            case 1:
                tvMon.setTextColor(getResources().getColor(R.color.orange_line));
                tvMon.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));
                break;
            case 2:
                tvMon.setTextColor(getResources().getColor(R.color.orange_line));
                tvMon.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineTue.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvTue.setTextColor(getResources().getColor(R.color.orange_line));
                tvTue.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));
                break;
            case 3:
                tvMon.setTextColor(getResources().getColor(R.color.orange_line));
                tvMon.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineTue.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvTue.setTextColor(getResources().getColor(R.color.orange_line));
                tvTue.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineWed.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvWed.setTextColor(getResources().getColor(R.color.orange_line));
                tvWed.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));
                break;
            case 4:
                tvMon.setTextColor(getResources().getColor(R.color.orange_line));
                tvMon.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineTue.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvTue.setTextColor(getResources().getColor(R.color.orange_line));
                tvTue.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineWed.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvWed.setTextColor(getResources().getColor(R.color.orange_line));
                tvWed.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineThu.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvThu.setTextColor(getResources().getColor(R.color.orange_line));
                tvThu.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));
                break;
            case 5:
                tvMon.setTextColor(getResources().getColor(R.color.orange_line));
                tvMon.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineTue.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvTue.setTextColor(getResources().getColor(R.color.orange_line));
                tvTue.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineWed.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvWed.setTextColor(getResources().getColor(R.color.orange_line));
                tvWed.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineThu.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvThu.setTextColor(getResources().getColor(R.color.orange_line));
                tvThu.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineFri.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvFri.setTextColor(getResources().getColor(R.color.orange_line));
                tvFri.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));
                break;
            case 6:
                tvMon.setTextColor(getResources().getColor(R.color.orange_line));
                tvMon.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineTue.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvTue.setTextColor(getResources().getColor(R.color.orange_line));
                tvTue.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineWed.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvWed.setTextColor(getResources().getColor(R.color.orange_line));
                tvWed.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineThu.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvThu.setTextColor(getResources().getColor(R.color.orange_line));
                tvThu.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineFri.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvFri.setTextColor(getResources().getColor(R.color.orange_line));
                tvFri.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineSat.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvSat.setTextColor(getResources().getColor(R.color.orange_line));
                tvSat.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));
                break;
            case 7:
                tvMon.setTextColor(getResources().getColor(R.color.orange_line));
                tvMon.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineTue.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvTue.setTextColor(getResources().getColor(R.color.orange_line));
                tvTue.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineWed.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvWed.setTextColor(getResources().getColor(R.color.orange_line));
                tvWed.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineThu.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvThu.setTextColor(getResources().getColor(R.color.orange_line));
                tvThu.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineFri.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvFri.setTextColor(getResources().getColor(R.color.orange_line));
                tvFri.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineSat.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvSat.setTextColor(getResources().getColor(R.color.orange_line));
                tvSat.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));

                lineSun.setBackgroundColor(getResources().getColor(R.color.orange_line));
                tvSun.setTextColor(getResources().getColor(R.color.orange_line));
                tvSun.setBackground(getResources().getDrawable(R.drawable.circle_orange_1));
                break;
            default:
                break;
        }
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
                sign();
                break;
            default:
                break;
        }
    }

    public void getSignRecord() {
        if (NetUtil.checkNetwork(this)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.getSignRecord(0, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        entitySignRecord =  JSON.parseObject(resultJson,EntitySignRecord.class);
                        if(entitySignRecord.isSuccess()){
                            //成功
                            //showShortToast(R.string.getSuccess);

                            initData();

                            progressBarDismiss();
                        }else{//显示失败信息
                            if (entitySignRecord.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entitySignRecord.getMessage());
                            }

                            progressBarDismiss();
                        }
                    }else{
                        showShortToast(R.string.noReturn);

                        progressBarDismiss();
                    }
                }
            });
        } else {
            showShortToast(R.string.checkNetwork);
        }
    }

    public void sign() {
        if (NetUtil.checkNetwork(this)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.postSign(0, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功
                            //showShortToast(R.string.getSuccess);

                            //continuityDay = + 1;
                            //weekDay = + 1;

                            getSignRecord();

                            progressBarDismiss();
                        }else{//显示失败信息
                            if (entityBase.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityBase.getMessage());
                            }

                            progressBarDismiss();
                        }
                    }else{
                        showShortToast(R.string.noReturn);

                        progressBarDismiss();
                    }
                }
            });
        } else {
            showShortToast(R.string.checkNetwork);
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
