package com.qingye.wtsyou.fragment.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.home.FansMainIdolAdapter;
import com.qingye.wtsyou.model.Stars;
import com.qingye.wtsyou.view.home.FansMainIdolView;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;

/**
 * A simple {@link Fragment} subclass.
 */
public class FansMainIdolFragment extends BaseHttpRecyclerFragment<Stars,FansMainIdolView,FansMainIdolAdapter> implements View.OnClickListener ,CacheCallBack<Stars> {

    private TextView tvMore,tvLess;

    private int size = 4;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static FansMainIdolFragment createInstance() {

        return new FansMainIdolFragment();
    }

    public FansMainIdolFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_fans_main_idol);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

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

        //实例化一个GridLayoutManager，列数为2
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        rvBaseRecycler.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void initView() {
        super.initView();
        tvMore = findViewById(R.id.tv_stars_more);
        tvLess = findViewById(R.id.tv_stars_less);
    }

    @Override
    public void setList(final List<Stars> list) {
        final List<Stars> templist = new ArrayList<>();
        for(int i = 0;i < size;i ++) {
            Stars stars = new Stars();
            stars.setId(i);
            templist.add(stars);
        }
        //list.addAll(templist);
        setList(new AdapterCallBack<FansMainIdolAdapter>() {

            @Override
            public FansMainIdolAdapter createAdapter() {
                return new FansMainIdolAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(templist);
            }
        });
    }

    @Override
    public void getListAsync(int page) {

    }

    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    public void initData() {//必须调用
        super.initData();

    }

    @Override
    public List<Stars> parseArray(String json) {
        return null;
    }

    @Override
    public Class<Stars> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(Stars data) {
        return null;
    }

    @Override
    public int getCacheCount() {
        return 0;
    }


    //Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    @Override
    public void initEvent() {//必须调用
        super.initEvent();
        tvMore.setOnClickListener(this);
        tvLess.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_stars_more:
                tvMore.setVisibility(View.GONE);
                tvLess.setVisibility(View.VISIBLE);
                size = 6;
                final List<Stars> templist1 = new ArrayList<>();
                for(int i = 0;i < size;i ++) {
                    Stars conversation = new Stars();
                    conversation.setId(i);
                    templist1.add(conversation);
                }
                setList(templist1);
                break;
            case R.id.tv_stars_less:
                tvMore.setVisibility(View.VISIBLE);
                tvLess.setVisibility(View.GONE);
                size = 4;
                final List<Stars> templist2 = new ArrayList<>();
                for(int i = 0;i < size;i ++) {
                    Stars conversation = new Stars();
                    conversation.setId(i);
                    templist2.add(conversation);
                }
                setList(templist2);
                break;
            default:
                break;
        }
    }
}
