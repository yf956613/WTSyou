package com.qingye.wtsyou.fragment.home;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.home.RecommendStarsConversationActivity;
import com.qingye.wtsyou.activity.home.StarsMainActivity;
import com.qingye.wtsyou.activity.search.SearchFansActivity;
import com.qingye.wtsyou.adapter.home.HomeContentAdapter;
import com.qingye.wtsyou.modle.Campaign;
import com.qingye.wtsyou.view.home.HomeContentView;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeStarsFragment extends BaseHttpRecyclerFragment<Campaign,HomeContentView,HomeContentAdapter> implements View.OnClickListener,CacheCallBack<Campaign>{

    private ImageView ivStars;
    private LinearLayout llfans,llconversation;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static HomeStarsFragment createInstance() {

        return new HomeStarsFragment();
    }

    public HomeStarsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_home_stars);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //禁止滑动
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);
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

        ivStars = findViewById(R.id.iv_stars_img);
        llfans = findView(R.id.ll_fans);
        llconversation = findView(R.id.ll_conversation);
    }

    @Override
    public void setList(final List<Campaign> list) {
        final List<Campaign> templist = new ArrayList<>();
        for(int i = 1;i < 8;i ++) {
            Campaign campaign = new Campaign();
            campaign.setId(i);
            templist.add(campaign);
        }
        //list.addAll(templist);
        setList(new AdapterCallBack<HomeContentAdapter>() {

            @Override
            public HomeContentAdapter createAdapter() {
                return new HomeContentAdapter(context);
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
    public List<Campaign> parseArray(String json) {
        return null;
    }

    @Override
    public Class<Campaign> getCacheClass() {
        return Campaign.class;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(Campaign data) {
        return null;
    }

    @Override
    public int getCacheCount() {
        return 4;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivStars.setOnClickListener(this);

        llfans.setOnClickListener(this);
        llconversation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_stars_img:

                break;
            case R.id.ll_fans:
                toActivity(SearchFansActivity.createIntent(context));
                break;
            case R.id.ll_conversation:
                toActivity(RecommendStarsConversationActivity.createIntent(context));
                break;
            default:
                break;
        }
    }
}
