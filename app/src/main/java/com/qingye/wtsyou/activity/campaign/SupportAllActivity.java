package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.fragment.activity.SupportAllFragment;

import zuo.biao.library.base.BaseTabActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class SupportAllActivity extends BaseTabActivity implements View.OnClickListener, OnBottomDragListener {

    private ImageView ivBack,ivSearch;
    private TextView tvHead;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context,SupportAllActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,this);
        setContentView(R.layout.activity_support_all,this);

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
        ivSearch = findViewById(R.id.iv_right);
        ivSearch.setImageResource(R.mipmap.search);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("应援列表");
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public String getTitleName() {
        return null;
    }

    @Override
    public String getReturnName() {
        return null;
    }

    @Override
    public String getForwardName() {
        return null;
    }

    @Override
    protected String[] getTabNames() {
        return new String[] {"应援中", "已结束"};
    }

    @Override
    protected Fragment getFragment(int position) {

        SupportAllFragment fragment = SupportAllFragment.createInstance();
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(SupportAllFragment.ARGUMENT_POSITION, position);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);

        topTabView.setOnTabSelectedListener(this);//覆盖super.initEvent();内的相同代码
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
    public void onDragBottom(boolean rightToLeft) {
        finish();
    }

    @Override
    public void onTabSelected(TextView tvTab, int position, int id) {
        super.onTabSelected(tvTab, position, id);
    }
}
