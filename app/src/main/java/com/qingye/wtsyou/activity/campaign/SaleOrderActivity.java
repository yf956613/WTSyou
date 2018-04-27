package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.my.CreateAddressActivity;
import com.qingye.wtsyou.adapter.campaign.SelectAddressAdapter;
import com.qingye.wtsyou.fragment.campaign.OrderDetailedFragment;
import com.qingye.wtsyou.modle.DeliveryAddress;
import com.qingye.wtsyou.view.campaign.SelectAddressView;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class SaleOrderActivity extends BaseHttpRecyclerActivity<DeliveryAddress,SelectAddressView,SelectAddressAdapter> implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private TextView tvHead;
    private ImageView ivBack;
    private LinearLayout btnDetailed;
    private LinearLayout llBottom;
    private FrameLayout flDetailed;

    //地址显示区域
    private LinearLayout llAddress;
    //选择地址区域
    private LinearLayout llSelectAddress;
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvCreateAddress;

    private ImageView ivArrow;
    private int narrowCount = 0;//记录明细是否显示

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, SaleOrderActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_order,this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>


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
        ivBack.setImageResource(R.mipmap.back_a);
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("订单详情");

        //查看明细
        btnDetailed = findViewById(R.id.btn_detailed);
        llBottom = findViewById(R.id.ll_bottom);
        ivArrow = findViewById(R.id.iv_arrow);
        flDetailed = findViewById(R.id.flDetailed);

        llAddress = findViewById(R.id.llAddress);
        //选择地址
        llSelectAddress = findViewById(R.id.llSelectAddress);
        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvCreateAddress = findViewById(R.id.tv_create_address);
    }

    @Override
    public void setList(List<DeliveryAddress> list) {
        final List<DeliveryAddress> templist = new ArrayList<>();
        for(int i = 1;i < 5;i ++) {
            DeliveryAddress deliveryAddress = new DeliveryAddress();
            deliveryAddress.setId(i);
            templist.add(deliveryAddress);
        }
        //list.addAll(templist);
        setList(new AdapterCallBack<SelectAddressAdapter>() {

            @Override
            public SelectAddressAdapter createAdapter() {
                return new SelectAddressAdapter(context);
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
    public List<DeliveryAddress> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
        btnDetailed.setOnClickListener(this);
        llAddress.setOnClickListener(this);
        llSelectAddress.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        tvCreateAddress.setOnClickListener(this);
    }

    public void getOrderDetailed() {
        OrderDetailedFragment orderDetailedFragment = new OrderDetailedFragment();
        //注意这里是调用getChildFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.flDetailed,orderDetailedFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_detailed:
                Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.gradually);
                narrowCount ++;
                if (narrowCount % 2 == 0) {
                    flDetailed.setVisibility(View.GONE);
                    ivArrow.setImageResource(R.mipmap.next_k);
                } else {
                    flDetailed.setVisibility(View.VISIBLE);
                    getOrderDetailed();
                    flDetailed.startAnimation(animation1);
                    ivArrow.setImageResource(R.mipmap.next_l);
                }
                break;
            case R.id.llAddress:
                Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.gradually);
                llSelectAddress.setVisibility(View.VISIBLE);
                llSelectAddress.setAnimation(animation2);
                break;
            case R.id.tv_cancel:
                llSelectAddress.setVisibility(View.GONE);
                break;
            case R.id.tv_create_address:
                toActivity(CreateAddressActivity.createIntent(context));
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
