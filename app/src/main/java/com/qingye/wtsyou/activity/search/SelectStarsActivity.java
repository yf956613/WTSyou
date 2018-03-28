package com.qingye.wtsyou.activity.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.home.SelectStarsAdapter;
import com.qingye.wtsyou.fragment.home.SelectedStarsFragment;
import com.qingye.wtsyou.modle.Stars;
import com.qingye.wtsyou.view.home.SelectStarsView;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class SelectStarsActivity extends BaseHttpRecyclerActivity <Stars,SelectStarsView,SelectStarsAdapter> implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivSearch;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context,SelectStarsActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stars,this);

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

        //实例化一个GridLayoutManager，列数为2
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        rvBaseRecycler.setLayoutManager(layoutManager);
    }

    @Override
    public void initView() {
        super.initView();
        ivBack = findViewById(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.backwirtih);
        ivSearch = findViewById(R.id.iv_right);
        ivSearch.setImageResource(R.mipmap.search);

        SelectedStarsFragment selectedStarsFragment = new SelectedStarsFragment();
        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_selected_stars,selectedStarsFragment);
        transaction.commit();
    }

    @Override
    public void setList(List<Stars> list) {
        final List<Stars> templist = new ArrayList<>();
        for(int i = 1;i < 6;i ++) {
            Stars stars = new Stars();
            stars.setId(i);
            templist.add(stars);
        }
        //list.addAll(templist);
        setList(new AdapterCallBack<SelectStarsAdapter>() {

            @Override
            public SelectStarsAdapter createAdapter() {
                return new SelectStarsAdapter(context);
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
    public List<Stars> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                toActivity(SearchStarsActivity.createIntent(context));
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
