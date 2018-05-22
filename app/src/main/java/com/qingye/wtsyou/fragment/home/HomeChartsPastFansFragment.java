package com.qingye.wtsyou.fragment.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.home.PastFansChartsDetailedActivity;
import com.qingye.wtsyou.adapter.home.HomeFansChartsPastAdapter;
import com.qingye.wtsyou.modle.FansCharts;
import com.qingye.wtsyou.view.home.HomeFansChartsPastView;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeChartsPastFansFragment extends BaseHttpRecyclerFragment<FansCharts,HomeFansChartsPastView,HomeFansChartsPastAdapter> implements CacheCallBack<FansCharts> {

    int Position;
    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static HomeChartsPastFansFragment createInstance() {

        return new HomeChartsPastFansFragment();
    }

    public HomeChartsPastFansFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_home_charts_past);

        /*//接收上一页传来的数据
        Bundle bundle = getArguments();
        Position = bundle.getInt(HomeChartsPastFansFragment.ARGUMENT_POSITION);*/

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

        return view;
    }



    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void setList(final List<FansCharts> list) {
        final List<FansCharts> templist = new ArrayList<>();
        for(int i = 1;i < 4;i ++) {
            FansCharts charts = new FansCharts();
            templist.add(charts);
        }
        //list.addAll(templist);
        setList(new AdapterCallBack<HomeFansChartsPastAdapter>() {

            @Override
            public HomeFansChartsPastAdapter createAdapter() {
                return new HomeFansChartsPastAdapter(context);
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
    public List<FansCharts> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {
        super.initEvent();
    }

    @Override
    public Class<FansCharts> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(FansCharts data) {
        return null;
    }

    @Override
    public int getCacheCount() {
        return 10;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (id > 0) {
            toActivity(PastFansChartsDetailedActivity.createIntent(context, id, Position));
        }
    }
}
