package com.qingye.wtsyou.fragment.campaign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.campaign.ConversationAdapter;

import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.view.campaign.ConversationView;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;

public class DetailedConversationFragment extends BaseHttpRecyclerFragment<ChatingRoom,ConversationView,ConversationAdapter>implements CacheCallBack<ChatingRoom> {

    private  List<ChatingRoom> chatingRooms =  new ArrayList<>();

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static DetailedConversationFragment createInstance() {

        return new DetailedConversationFragment();
    }

    public DetailedConversationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_campaign_detailed_conversation);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        //获取传来的数据
        Bundle bundle = getArguments();
        chatingRooms = (List<ChatingRoom>) bundle.getSerializable(Constant.CHATINTROOMLIST);

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

        setList(chatingRooms);

        return view;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void setList(final List<ChatingRoom> list) {

        setList(new AdapterCallBack<ConversationAdapter>() {

            @Override
            public ConversationAdapter createAdapter() {
                return new ConversationAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
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
    public List<ChatingRoom> parseArray(String json) {
        return null;
    }

    @Override
    public Class<ChatingRoom> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(ChatingRoom data) {
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
