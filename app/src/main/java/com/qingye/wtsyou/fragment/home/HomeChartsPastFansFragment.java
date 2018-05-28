package com.qingye.wtsyou.fragment.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.home.PastFansChartsDetailedActivity;
import com.qingye.wtsyou.adapter.home.HomeFansChartsPastAdapter;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.EntityRankInfo;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.view.home.HomeFansChartsPastView;
import zuo.biao.library.widget.CustomDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeChartsPastFansFragment extends BaseHttpRecyclerFragment<EntityRankInfo,HomeFansChartsPastView,HomeFansChartsPastAdapter> implements CacheCallBack<EntityRankInfo> {

    private int currentPage = 1;
    private final int pageSize = 12;
    private int totalPage = 0;
    //是否降序
    private final Boolean desc = false;

    private CustomDialog progressBar;

    private List<EntityRankInfo> rankInfoList = new ArrayList<>();

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

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        historyRankingQuery();

        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>


        srlBaseHttpRecycler.setEnableRefresh(true);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(true);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(true);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(true);//尾部
        //srlBaseHttpRecycler.autoRefresh();

        return view;
    }



    @Override
    public void initView() {
        super.initView();
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
    public void setList(final List<EntityRankInfo> list) {

        setList(new AdapterCallBack<HomeFansChartsPastAdapter>() {

            @Override
            public HomeFansChartsPastAdapter createAdapter() {
                return new HomeFansChartsPastAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
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
    public List<EntityRankInfo> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {
        super.initEvent();
    }

    @Override
    public Class<EntityRankInfo> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(EntityRankInfo data) {
        return null;
    }

    @Override
    public int getCacheCount() {
        return 10;
    }

    public void historyRankingQuery(){
        historyRankingQuery(1);
    }

    public void historyRankingQuery(final int page) {
        if (NetUtil.checkNetwork(getActivity())) {
            setProgressBar();
            progressBar.show();

            String keywords = null;
            String periods = null;
            String maxPeriods = null;

            HttpRequest.postHistoryFansWeekRank(0, page, pageSize, desc, keywords, periods, maxPeriods, new OnHttpResponseListener() {

                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                    if(!StringUtil.isEmpty(resultJson)){

                        EntityPageData entityPageData =  JSON.parseObject(resultJson,EntityPageData.class);

                        if(entityPageData.isSuccess()){

                            //成功
                            //showShortToast(R.string.getSuccess);
                            if (page == 1) {
                                currentPage = 1;
                                rankInfoList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageData.getContent().getData())
                                        ,new TypeToken<List<EntityRankInfo>>(){}.getType());

                                srlBaseHttpRecycler.finishRefresh();
                                srlBaseHttpRecycler.setLoadmoreFinished(false);

                            } else {

                                List<EntityRankInfo> entityRankInfos = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageData.getContent().getData())
                                        ,new TypeToken<List<EntityRankInfo>>(){}.getType());

                                if (rankInfoList.size() == 0) {
                                    srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();
                                } else {
                                    rankInfoList.addAll(entityRankInfos);
                                    srlBaseHttpRecycler.finishLoadmore();
                                }
                            }

                            progressBarDismiss();

                            setList(rankInfoList);

                            totalPage = entityPageData.getContent().getPageCount();

                            currentPage ++;

                        }else{//显示失败信息
                            if (entityPageData.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityPageData.getMessage());
                            }

                            progressBarDismiss();
                        }

                    }else{
                        showShortToast(R.string.noReturn);

                        progressBarDismiss();
                    }
                }
            });
        } else {
            showShortToast(R.string.checkNetwork);

            progressBarDismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toActivity(PastFansChartsDetailedActivity.createIntent(context, rankInfoList.get(position).getPeriods(), rankInfoList.get(position).getPeriodsZone()));
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        historyRankingQuery();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        historyRankingQuery(currentPage);
    }
}
