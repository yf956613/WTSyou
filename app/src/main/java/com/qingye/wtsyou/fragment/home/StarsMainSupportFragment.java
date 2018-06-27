package com.qingye.wtsyou.fragment.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.campaign.ActivityNewSupportAdapter;
import com.qingye.wtsyou.model.Supports;
import com.qingye.wtsyou.view.campaign.ActivityNewSupportView;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;

/**
 * A simple {@link Fragment} subclass.
 */
public class StarsMainSupportFragment extends BaseHttpRecyclerFragment<Supports,ActivityNewSupportView,ActivityNewSupportAdapter> implements CacheCallBack<Supports> {

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static StarsMainSupportFragment createInstance() {

        return new StarsMainSupportFragment();
    }

    public StarsMainSupportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_stars_main);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //禁止滑动
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);
        //linearLayoutManager.setScrollEnabled(false);
        rvBaseRecycler.setNestedScrollingEnabled(false);//解决卡顿
        rvBaseRecycler.setLayoutManager(linearLayoutManager);

        //srlBaseHttpRecycler.autoRefresh();
        srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(false);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(false);//尾部

        return view;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void setList(final List<Supports> list) {
        final List<Supports> templist = new ArrayList<>();
        for(int i = 1;i < 4;i ++) {
            Supports campaign = new Supports();
            templist.add(campaign);
        }
        //list.addAll(templist);
        setList(new AdapterCallBack<ActivityNewSupportAdapter>() {

            @Override
            public ActivityNewSupportAdapter createAdapter() {
                return new ActivityNewSupportAdapter(context);
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
    public List<Supports> parseArray(String json) {
        return null;
    }

    @Override
    public Class<Supports> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(Supports data) {
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

    }

    //点击item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //toActivity(SupportDetailedActivity.createIntent(context,id));
    }

}
