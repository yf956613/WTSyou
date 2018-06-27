package com.qingye.wtsyou.fragment.campaign;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.campaign.SaleDetailedActivity;
import com.qingye.wtsyou.adapter.campaign.StarsCampaignShowAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.model.Concert;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.EntitySaleDetailed;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.campaign.StarsCampaignShowView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import com.qingye.wtsyou.manager.HttpPageModel;
import zuo.biao.library.widget.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class StarsCampaignShowFragment extends BaseHttpRecyclerFragment<Concert,StarsCampaignShowView,StarsCampaignShowAdapter>
        implements CacheCallBack<Concert>, OnHttpPageCallBack<EntityPageData, Concert> {

    private LinearLayout noMessage;

    private HttpPageModel<EntityPageData, Concert> mEntityPageDataHttpModel;

    private CustomDialog progressBar;

    private List<Concert> concertList = new ArrayList<>();

    String cityName = null;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static StarsCampaignShowFragment createInstance() {

        return new StarsCampaignShowFragment();
    }

    public StarsCampaignShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_stars_campaign);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        //获取activity传来的数据
        Bundle bundle = getArguments();
        cityName = bundle.getString(Constant.CITYNAME);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //演唱会列表
        mEntityPageDataHttpModel = new HttpPageModel<>(EntityPageData.class);
        concertQuery();

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

        noMessage = findViewById(R.id.noMessage);
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
    public void setList(final List<Concert> list) {

        setList(new AdapterCallBack<StarsCampaignShowAdapter>() {

            @Override
            public StarsCampaignShowAdapter createAdapter() {
                return new StarsCampaignShowAdapter(context);
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
    public List<Concert> parseArray(String json) {
        return null;
    }

    @Override
    public Class<Concert> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(Concert data) {
        return null;
    }


    //Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    @Override
    public void initEvent() {//必须调用
        super.initEvent();

    }

    @Override
    public int getCacheCount() {
        return 0;
    }

    //点击item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String uuid = concertList.get(position).getActivityId();
        toActivity(SaleDetailedActivity.createIntent(context, concertList.get(position).getActivityId()));

    }

    public void concertQuery() {
        /*setProgressBar();
        progressBar.show();*/

        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.CONCERTQUERY, this);

    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public List<Concert> getList(EntityPageData data) {
        return GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(data.getContent().getData())
                ,new TypeToken<List<Concert>>(){}.getType());
    }

    @Override
    public String getRequestJsonStr(int page, int pageSize) {
        String activityStates = null;
        String relevanceStar = null;
        String activityProperty = "concert";
        String createUserId = null;
        String parts = "prices";

        String request = HttpRequest.postConcertQuery(activityStates, relevanceStar, cityName,
                activityProperty, createUserId, parts, page, pageSize);
        return request;
    }

    @Override
    public void emptyPagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishRefresh();

        if (concertList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refreshSuccessPagingList(List<Concert> list) {
        concertList.clear();

        concertList.addAll(list);
        srlBaseHttpRecycler.finishRefresh();
        srlBaseHttpRecycler.setLoadmoreFinished(false);

        setList(concertList);

        if (concertList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void noMorePagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();

        if (concertList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadMoreSuccessPagingList(List<Concert> list) {
        concertList.addAll(list);
        srlBaseHttpRecycler.finishLoadmore();

        setList(concertList);

        if (concertList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refreshErrorPagingList() {
        showShortToast(R.string.noReturn);

        if (concertList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadMoreErrorPagingList() {
        showShortToast(R.string.noReturn);

        if (concertList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        //演唱会列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.CONCERTQUERY, this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //演唱会列表
        mEntityPageDataHttpModel.loadMorePost(URL_BASE + URLConstant.CONCERTQUERY, this);
    }

}
