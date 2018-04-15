package com.qingye.wtsyou.fragment.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.home.FansMainActivity;
import com.qingye.wtsyou.activity.home.StarsMainActivity;
import com.qingye.wtsyou.adapter.home.HomeChartsAdapter;
import com.qingye.wtsyou.modle.Charts;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpListFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeChartsFansFragment extends BaseHttpListFragment<Charts,HomeChartsAdapter> implements CacheCallBack<Charts> {

    int Position;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static HomeChartsFansFragment createInstance() {

        return new HomeChartsFansFragment();
    }

    public HomeChartsFansFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_home_charts);

        /*//接收上一页传来的数据
        Bundle bundle = getArguments();
        Position = bundle.getInt(HomeChartsFansFragment.ARGUMENT_POSITION);*/

        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //lvBaseList.onRefresh();
        lvBaseList.setPullLoadEnable(false);//下拉不加载更多
        lvBaseList.setPullRefreshEnable(false);//下拉不刷新

        return view;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void setList(List<Charts> list) {
        final List<Charts> templist = new ArrayList<>();
        for(int i = 1;i < 8;i ++) {
            Charts charts = new Charts();
            charts.setId(i);
            templist.add(charts);
        }
        //list.addAll(templist);
        setList(new AdapterCallBack<HomeChartsAdapter>() {

            @Override
            public HomeChartsAdapter createAdapter() {
                return new HomeChartsAdapter(context);
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
    public List<Charts> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {
        super.initEvent();
    }

    @Override
    public Class<Charts> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(Charts data) {
        return null;
    }

    @Override
    public int getCacheCount() {
        return 10;
    }

    //点击item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toActivity(FansMainActivity.createIntent(context, id));
    }
}
