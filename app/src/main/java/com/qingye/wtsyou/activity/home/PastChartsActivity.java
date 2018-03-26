package com.qingye.wtsyou.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.fragment.home.HomeChartsPastFragment;

import zuo.biao.library.base.BaseTabActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class PastChartsActivity extends BaseTabActivity implements View.OnClickListener, OnBottomDragListener {

    private ImageView ivBack,ivRules;
    private TextView tvHead;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context,PastChartsActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_charts,this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        super.initView();
        ivBack = findViewById(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        ivRules = findViewById(R.id.iv_right);
        ivRules.setImageResource(R.mipmap.rule);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("往期排行榜");
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Nullable
    @Override
    public String getTitleName() {
        return null;
    }

    @Nullable
    @Override
    public String getReturnName() {
        return null;
    }

    @Nullable
    @Override
    public String getForwardName() {
        return null;
    }

    @Override
    protected String[] getTabNames() {
        return new String[] {"明星排行榜", "粉丝贡献榜"};
    }

    @Override
    protected Fragment getFragment(int position) {
        HomeChartsPastFragment fragment = HomeChartsPastFragment.createInstance();
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(HomeChartsPastFragment.ARGUMENT_POSITION, position);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
        ivRules.setOnClickListener(this);

        topTabView.setOnTabSelectedListener(this);//覆盖super.initEvent();内的相同代码
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
    public void onDragBottom(boolean rightToLeft) {

    }

}
