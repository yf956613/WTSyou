package com.qingye.wtsyou.fragment.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.conversation.ConversationHotAdapter;
import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.view.conversation.ConversationHotView;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;

/**
 * A simple {@link Fragment} subclass.
 */
public class FansMainConversationFragment extends BaseHttpRecyclerFragment<ChatingRoom,ConversationHotView,ConversationHotAdapter> implements View.OnClickListener , CacheCallBack<ChatingRoom> {

    private TextView tvMore,tvLess;

    private  List<ChatingRoom> chatingRoomList =  new ArrayList<>();

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static FansMainConversationFragment createInstance() {

        return new FansMainConversationFragment();
    }

    public FansMainConversationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_fans_main);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

        //获取传来的数据
        Bundle bundle = getArguments();
        chatingRoomList = (List<ChatingRoom>) bundle.getSerializable(Constant.CONVERSATIONLIST);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
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
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rvBaseRecycler.setLayoutManager(layoutManager);

        List<ChatingRoom> fansIdolList = new ArrayList<>();
        if (chatingRoomList != null) {
            if (chatingRoomList.size() > 2) {
                for (int i = 0;i < 2;i ++) {
                    fansIdolList.add(chatingRoomList.get(i));
                }
                setList(fansIdolList);
                tvMore.setEnabled(true);
            } else {
                fansIdolList.addAll(chatingRoomList);
                setList(fansIdolList);
                tvMore.setEnabled(false);
            }
        } else {
            tvMore.setEnabled(false);
        }


        return view;
    }

    @Override
    public void initView() {
        super.initView();
        tvMore = findViewById(R.id.tv_more);
        tvLess = findViewById(R.id.tv_less);
    }

    @Override
    public void setList(final List<ChatingRoom> list) {

        setList(new AdapterCallBack<ConversationHotAdapter>() {

            @Override
            public ConversationHotAdapter createAdapter() {
                return new ConversationHotAdapter(context);
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
        tvMore.setOnClickListener(this);
        tvLess.setOnClickListener(this);
    }

    public void setMoreConversationList(int size) {
        List<ChatingRoom> chatingRooms = new ArrayList<>();
        if (size > 2) {
            chatingRooms.addAll(chatingRoomList);
            setList(chatingRooms);
        } else {
            tvMore.setEnabled(false);
        }
    }

    public void setLessConversationList(int size) {
        List<ChatingRoom> chatingRooms = new ArrayList<>();
        if (size > 2) {
            for (int i = 0;i < 2;i ++) {
                chatingRooms.add(chatingRoomList.get(i));
            }
            setList(chatingRooms);
        } else {
            chatingRooms.addAll(chatingRoomList);
            setList(chatingRooms);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more:
                tvMore.setVisibility(View.GONE);
                tvLess.setVisibility(View.VISIBLE);
                setMoreConversationList(chatingRoomList.size());
                break;
            case R.id.tv_less:
                tvMore.setVisibility(View.VISIBLE);
                tvLess.setVisibility(View.GONE);
                setLessConversationList(chatingRoomList.size());
                break;
            default:
                break;
        }
    }
}
