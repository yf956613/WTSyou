package com.qingye.wtsyou.fragment.home;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.campaign.CrowdDetailedActivity;
import com.qingye.wtsyou.activity.campaign.SaleDetailedActivity;
import com.qingye.wtsyou.activity.campaign.SupportDetailedActivity;
import com.qingye.wtsyou.activity.campaign.VoteDetailedActivity;
import com.qingye.wtsyou.adapter.campaign.FansMainActivityAdapter;
import com.qingye.wtsyou.model.Activitys;
import com.qingye.wtsyou.model.EntityCrowdDetailed;
import com.qingye.wtsyou.model.EntitySaleDetailed;
import com.qingye.wtsyou.model.EntitySupportDetailed;
import com.qingye.wtsyou.model.EntityVoteDetailed;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.view.campaign.FansMainActivityView;
import com.qingye.wtsyou.widget.StartSnapHelper;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.widget.CustomDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class FansMainCampaignFragment extends BaseHttpRecyclerFragment<Activitys,FansMainActivityView,FansMainActivityAdapter> implements View.OnClickListener ,CacheCallBack<Activitys> {

    private  List<Activitys> activitysList =  new ArrayList<>();

    private CustomDialog progressBar;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static FansMainCampaignFragment createInstance() {

        return new FansMainCampaignFragment();
    }

    public FansMainCampaignFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_fans_main_campaign);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        initCache(this);

        //获取传来的数据
        Bundle bundle = getArguments();
        activitysList = (List<Activitys>) bundle.getSerializable(Constant.ACTIVITYLIST);

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

        StartSnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(rvBaseRecycler);

        setList(activitysList);

        return view;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void setList(final List<Activitys> list) {

        setList(new AdapterCallBack<FansMainActivityAdapter>() {

            @Override
            public FansMainActivityAdapter createAdapter() {
                return new FansMainActivityAdapter(context);
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

    public void onResume() {

        super.onResume();
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
    public List<Activitys> parseArray(String json) {
        return null;
    }

    @Override
    public Class<Activitys> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(Activitys data) {
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

    @Override
    public void onClick(View v) {

    }

    //点击item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

        //检查网络
        if (NetUtil.checkNetwork(context)) {
            String activityState = activitysList.get(position).getState();
            String uuid = activitysList.get(position).getActivityId();

            setProgressBar();
            progressBar.show();

            if (activityState.equals("voting") || activityState.equals("votesuccess") || activityState.equals("votefail")) {

                toActivity(VoteDetailedActivity.createIntent(context, uuid));
            }
            else if (activityState.equals("crowding") || activityState.equals("crowdsuccess") || activityState.equals("crowdfail")) {

                toActivity(CrowdDetailedActivity.createIntent(context, uuid));
            }
            else if (activityState.equals("saling")) {

                toActivity(SaleDetailedActivity.createIntent(context, uuid));
            }
            else if (activityState.equals("supporting") || activityState.equals("supportsuccess") || activityState.equals("supportfail")) {
                toActivity(SupportDetailedActivity.createIntent(context, uuid));
            }
            else if (activityState.equals("unaudited")) {
                showShortToast("这是个未审核的活动~");
            }
        } else {
            showShortToast(R.string.checkNetwork);
        }

    }
}
