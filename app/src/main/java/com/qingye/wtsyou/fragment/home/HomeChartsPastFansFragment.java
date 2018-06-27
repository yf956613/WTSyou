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
import com.qingye.wtsyou.activity.home.PastFansChartsDetailedActivity;
import com.qingye.wtsyou.adapter.home.HomeFansChartsPastAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.RankInfo;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.home.HomeFansChartsPastView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeChartsPastFansFragment extends BaseHttpRecyclerFragment<RankInfo,HomeFansChartsPastView,HomeFansChartsPastAdapter>
        implements CacheCallBack<RankInfo>, OnHttpPageCallBack<EntityPageData, RankInfo> {

    private HttpPageModel<EntityPageData, RankInfo> mEntityPageDataHttpModel;

    //是否降序
    private final Boolean desc = false;

    private CustomDialog progressBar;

    private List<RankInfo> rankInfoList = new ArrayList<>();

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

        //往期查询
        mEntityPageDataHttpModel = new HttpPageModel<>(EntityPageData.class);
        historyRankingQuery();

        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        srlBaseHttpRecycler.autoRefresh();
        /*srlBaseHttpRecycler.setEnableRefresh(true);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(true);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(true);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(true);//尾部*/

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
    public void setList(final List<RankInfo> list) {

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
    public List<RankInfo> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {
        super.initEvent();
    }

    @Override
    public Class<RankInfo> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(RankInfo data) {
        return null;
    }

    @Override
    public int getCacheCount() {
        return 10;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toActivity(PastFansChartsDetailedActivity.createIntent(context, rankInfoList.get(position).getPeriods(), rankInfoList.get(position).getPeriodsZone()));
    }

    public void historyRankingQuery() {
        /*setProgressBar();
        progressBar.show();*/

        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.HISTORYFANSWEEKRANKING, this);
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public List<RankInfo> getList(EntityPageData data) {
        return GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(data.getContent().getData())
                ,new TypeToken<List<RankInfo>>(){}.getType());
    }

    @Override
    public String getRequestJsonStr(int page, int pageSize) {
        String keywords = null;
        String periods = null;
        String maxPeriods = null;

        String request = HttpRequest.postRankQuery(page, pageSize, desc,
                keywords,periods,maxPeriods);
        return request;
    }

    @Override
    public void emptyPagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishRefresh();
    }

    @Override
    public void refreshSuccessPagingList(List<RankInfo> list) {
        rankInfoList.clear();

        rankInfoList.addAll(list);
        srlBaseHttpRecycler.finishRefresh();
        srlBaseHttpRecycler.setLoadmoreFinished(false);

        setList(rankInfoList);
    }

    @Override
    public void noMorePagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();
    }

    @Override
    public void loadMoreSuccessPagingList(List<RankInfo> list) {
        rankInfoList.addAll(list);
        srlBaseHttpRecycler.finishLoadmore();

        setList(rankInfoList);
    }

    @Override
    public void refreshErrorPagingList() {
        showShortToast(R.string.noReturn);
    }

    @Override
    public void loadMoreErrorPagingList() {
        showShortToast(R.string.noReturn);
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        //往期列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.HISTORYFANSWEEKRANKING, this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //往期列表
        mEntityPageDataHttpModel.loadMorePost(URL_BASE + URLConstant.HISTORYFANSWEEKRANKING, this);
    }
}
