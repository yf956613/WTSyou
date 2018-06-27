package com.qingye.wtsyou.fragment.campaign;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.campaign.CrowdDetailedActivity;
import com.qingye.wtsyou.activity.campaign.SaleDetailedActivity;
import com.qingye.wtsyou.activity.campaign.VoteDetailedActivity;
import com.qingye.wtsyou.adapter.campaign.ActivityHotShowAdapter;
import com.qingye.wtsyou.model.Hots;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.view.campaign.ActivityHotShowView;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.widget.CustomDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityHotShowFragment extends BaseHttpRecyclerFragment<Hots,ActivityHotShowView,ActivityHotShowAdapter> implements CacheCallBack<Hots> {

    private List<Hots> hotCampaigns =  new ArrayList<>();

    private CustomDialog progressBar;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static ActivityHotShowFragment createInstance() {

        return new ActivityHotShowFragment();
    }

    public ActivityHotShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_activity_hot_show);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

        //获取传来的数据
        Bundle bundle = getArguments();
        hotCampaigns = (List<Hots>) bundle.getSerializable(Constant.HOTCAMPAIGN);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvBaseRecycler.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        srlBaseHttpRecycler.autoRefresh();
        setList(hotCampaigns);

        return view;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }

            progressBar = null;
        }
    }

    private void setProgressBar() {
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void progressBarDismiss() {
        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
                progressBar.cancel();
            }
        }
    }

    @Override
    public void setList(final List<Hots> list) {

        setList(new AdapterCallBack<ActivityHotShowAdapter>() {

            @Override
            public ActivityHotShowAdapter createAdapter() {
                return new ActivityHotShowAdapter(context);
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
    public List<Hots> parseArray(String json) {
        return null;
    }

    @Override
    public Class<Hots> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(Hots data) {
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

        String activityState = hotCampaigns.get(position).getActivityState();
        String uuid = hotCampaigns.get(position).getActivityId();

        if (activityState.equals("voting") || activityState.equals("votesuccess") || activityState.equals("votefail")) {

            toActivity(VoteDetailedActivity.createIntent(context, uuid));
        }
        else if (activityState.equals("crowding") || activityState.equals("crowdsuccess") || activityState.equals("crowdfail")) {

            toActivity(CrowdDetailedActivity.createIntent(context, uuid));
        }
        else if (activityState.equals("saling") || activityState.equals("over")) {
            toActivity(SaleDetailedActivity.createIntent(context,uuid));
        }

    }
}
