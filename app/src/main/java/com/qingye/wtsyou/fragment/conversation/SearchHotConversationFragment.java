package com.qingye.wtsyou.fragment.conversation;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.search.SearchHotConversationAdapter;
import com.qingye.wtsyou.model.HotTopic;
import com.qingye.wtsyou.view.search.SearchHotConversationView;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;

public class SearchHotConversationFragment extends BaseHttpRecyclerFragment<HotTopic,SearchHotConversationView,SearchHotConversationAdapter>implements CacheCallBack<HotTopic> {

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static SearchHotConversationFragment createInstance() {

        return new SearchHotConversationFragment();
    }

    public SearchHotConversationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_campaign_detailed_conversation);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

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
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rvBaseRecycler.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void setList(final List<HotTopic> list) {
        final List<HotTopic> templist = new ArrayList<>();
        for(int i = 1;i < 9;i ++) {
            HotTopic conversation = new HotTopic();
            templist.add(conversation);
        }
        //list.addAll(templist);
        setList(new AdapterCallBack<SearchHotConversationAdapter>() {

            @Override
            public SearchHotConversationAdapter createAdapter() {
                return new SearchHotConversationAdapter(context);
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
    public List<HotTopic> parseArray(String json) {
        return null;
    }

    @Override
    public Class<HotTopic> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(HotTopic data) {
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
}
