package com.qingye.wtsyou.fragment.campaign;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.campaign.DetailedActivity;
import com.qingye.wtsyou.adapter.campaign.ActivityOfficialAdapter;
import com.qingye.wtsyou.model.Officials;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.view.campaign.ActivityOfficialView;
import com.qingye.wtsyou.widget.StartSnapHelper;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.ui.WebViewActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityOfficialFragment extends BaseHttpRecyclerFragment<Officials,ActivityOfficialView,ActivityOfficialAdapter> implements CacheCallBack<Officials> {

    private  List<Officials> officials =  new ArrayList<>();

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static ActivityOfficialFragment createInstance() {

        return new ActivityOfficialFragment();
    }

    public ActivityOfficialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_activity_official);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

        //获取传来的数据
        Bundle bundle = getArguments();
        officials = (List<Officials>) bundle.getSerializable(Constant.OFFICIALS);

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
        rvBaseRecycler.setFocusableInTouchMode(false);
        rvBaseRecycler.requestFocus();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvBaseRecycler.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        StartSnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(rvBaseRecycler);

        setList(officials);

        return view;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void setList(final List<Officials> list) {
        setList(new AdapterCallBack<ActivityOfficialAdapter>() {

            @Override
            public ActivityOfficialAdapter createAdapter() {
                return new ActivityOfficialAdapter(context);
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
    public List<Officials> parseArray(String json) {
        return null;
    }

    @Override
    public Class<Officials> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(Officials data) {
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
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
        String title = officials.get(position).getActivityName();
        if (officials.get(position).getType().equals("url")) {
            String url = officials.get(position).getUrl();
            toActivity(WebViewActivity.createIntent(context, title, url));
        }
        else if (officials.get(position).getType().equals("h5")){
            String content = officials.get(position).getDetail();
            toActivity(DetailedActivity.createIntent(context, title, content));
        }
    }
}
