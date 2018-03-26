package com.qingye.wtsyou.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.fragment.home.HomeChartsPastDetailedFragment;
import com.qingye.wtsyou.utils.Constant;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.ui.BottomMenuView;

public class PastChartsDetailedActivity extends BaseActivity implements View.OnClickListener, OnBottomDragListener
        , BottomMenuView.OnBottomMenuItemClickListener, OnHttpResponseListener {

    private ImageView ivBack,ivRules;
    private TextView tvHead;

    private int Position = 0;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @param id
     * @return
     */
    public static Intent createIntent(Context context, long id, int Position) {
        return new Intent(context,PastChartsDetailedActivity.class).putExtra("POSITION",Position);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_charts_detailed,this);

        //获取intent中的数据
        intent = getIntent();
        Position = intent.getIntExtra(Constant.TAB_FRAGMENT_POSITION,Position);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void onDragBottom(boolean rightToLeft) {

    }

    @Override
    public void initView() {
        ivBack = findViewById(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        ivRules = findViewById(R.id.iv_right);
        ivRules.setImageResource(R.mipmap.rule);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("往期排行榜");

        //activity往fragment传递数据
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.TAB_FRAGMENT_POSITION, Position);

        HomeChartsPastDetailedFragment homeChartsPastDetailedFragment = new HomeChartsPastDetailedFragment();
        homeChartsPastDetailedFragment.setArguments(bundle);

        //注意这里是调用getChildFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_charts,homeChartsPastDetailedFragment);
        transaction.commit();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
        ivRules.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                toActivity(PastChartsRuleActivity.createIntent(context));
                break;
            default:
                break;
        }
    }

    @Override
    public void onHttpResponse(int requestCode, String resultJson, Exception e) {

    }

    @Override
    public void onBottomMenuItemClick(int intentCode) {

    }
}
