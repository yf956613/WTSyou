package com.qingye.wtsyou.fragment.campaign;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.campaign.SupportDetailedActivity;
import com.qingye.wtsyou.adapter.campaign.ActivityNewSupportAdapter;
import com.qingye.wtsyou.model.EntitySupportDetailed;
import com.qingye.wtsyou.model.Supports;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.view.campaign.ActivityNewSupportView;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;

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
public class SearchCampaignSupportFragment extends BaseHttpRecyclerFragment<Supports,ActivityNewSupportView,ActivityNewSupportAdapter> implements CacheCallBack<Supports> {

    private  List<Supports> supports =  new ArrayList<>();

    private CustomDialog progressBar;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static SearchCampaignSupportFragment createInstance() {

        return new SearchCampaignSupportFragment();
    }

    public SearchCampaignSupportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_search_campaign_support);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //获取传来的数据
        Bundle bundle = getArguments();
        supports = (List<Supports>) bundle.getSerializable(Constant.SUPPORTLIST);

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

        setList(supports);

        return view;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void setList(final List<Supports> list) {

        setList(new AdapterCallBack<ActivityNewSupportAdapter>() {

            @Override
            public ActivityNewSupportAdapter createAdapter() {
                return new ActivityNewSupportAdapter(context);
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

        toActivity(SupportDetailedActivity.createIntent(context, supports.get(position).getActivityId()));
    }

}
