package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.home.PastChartsRuleActivity;
import com.qingye.wtsyou.adapter.my.DiamondDetailedAdapter;
import com.qingye.wtsyou.adapter.my.HeartDetailedAdapter;
import com.qingye.wtsyou.modle.DiamondDetailed;
import com.qingye.wtsyou.modle.HeartDetailed;
import com.qingye.wtsyou.view.my.DiamondDetailedView;
import com.qingye.wtsyou.view.my.HeartDetailedView;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class DiamondActivity extends BaseHttpRecyclerActivity<DiamondDetailed,DiamondDetailedView,DiamondDetailedAdapter> implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivRule;
    private TextView tvHead;
    private Button btnRecharge;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context,DiamondActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diamond,this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //禁止滑动
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);
        linearLayoutManager.setScrollEnabled(false);
        rvBaseRecycler.setNestedScrollingEnabled(false);//解决卡顿
        rvBaseRecycler.setLayoutManager(linearLayoutManager);

        //srlBaseHttpRecycler.autoRefresh();
        srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(false);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(false);//尾部

    }

    @Override
    public void initView() {
        super.initView();
        ivBack = findViewById(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_l);
        ivRule = findViewById(R.id.iv_right);
        ivRule.setImageResource(R.mipmap.rule_w);
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("我的钻石");
        tvHead.setTextColor(getResources().getColor(R.color.white));
        btnRecharge = findViewById(R.id.btn_recharge);
    }

    @Override
    public void setList(List<DiamondDetailed> list) {
        final List<DiamondDetailed> templist = new ArrayList<>();
        for(int i = 1;i < 9;i ++) {
            DiamondDetailed diamondDetailed = new DiamondDetailed();
            diamondDetailed.setId(i);
            templist.add(diamondDetailed);
        }
        //list.addAll(templist);
        setList(new AdapterCallBack<DiamondDetailedAdapter>() {

            @Override
            public DiamondDetailedAdapter createAdapter() {
                return new DiamondDetailedAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(templist);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void getListAsync(int page) {

    }

    @Override
    public List<DiamondDetailed> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
        ivRule.setOnClickListener(this);
        btnRecharge.setOnClickListener(this);
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
            case R.id.btn_recharge:
                toActivity(RechargeActivity.createIntent(context));
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
