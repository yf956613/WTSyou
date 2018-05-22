package com.qingye.wtsyou.fragment.campaign;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.campaign.CrowdDetailedActivity;
import com.qingye.wtsyou.adapter.home.StarsMainCrowdAdapter;
import com.qingye.wtsyou.modle.Crowd;
import com.qingye.wtsyou.modle.EntityCrowdDetailed;
import com.qingye.wtsyou.modle.EntityPageData;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.view.home.StarsMainCrowdView;
import com.qingye.wtsyou.widget.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

public class StarsCampaignCrowdFragment extends BaseHttpRecyclerFragment<Crowd,StarsMainCrowdView,StarsMainCrowdAdapter> implements CacheCallBack<Crowd> {

    private CustomDialog progressBar;

    private List<Crowd> crowdList = new ArrayList<>();
    String cityName = null;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static StarsCampaignCrowdFragment createInstance() {

        return new StarsCampaignCrowdFragment();
    }

    public StarsCampaignCrowdFragment() {
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

        crowdQuery();

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
    public void setList(final List<Crowd> list) {

        setList(new AdapterCallBack<StarsMainCrowdAdapter>() {

            @Override
            public StarsMainCrowdAdapter createAdapter() {
                return new StarsMainCrowdAdapter(context);
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
    public List<Crowd> parseArray(String json) {
        return null;
    }

    @Override
    public Class<Crowd> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(Crowd data) {
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

    public void crowdQuery() {
        if (NetUtil.checkNetwork(getActivity())) {
            setProgressBar();
            progressBar.show();

            String activityStates = null;
            String relevanceStar = null;
            String activityProperty = "crowd";
            String createUserId = null;

            HttpRequest.postCrowdQuery(0, activityStates, relevanceStar, cityName, activityProperty,
                    createUserId, new OnHttpResponseListener() {

                        @Override
                        public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                            if(!StringUtil.isEmpty(resultJson)){
                                EntityPageData entityPageData =  JSON.parseObject(resultJson,EntityPageData.class);

                                if(entityPageData.isSuccess()){
                                    //成功
                                    //showShortToast(R.string.getSuccess);
                                    crowdList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageData.getContent().getData())
                                            ,new TypeToken<List<Crowd>>(){}.getType());

                                    setList(crowdList);

                                    progressBarDismiss();
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

    //点击item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //检查网络
        if (NetUtil.checkNetwork(context)) {
            String uuid = crowdList.get(position).getActivityId();

            setProgressBar();
            progressBar.show();

            HttpRequest.getCrowdDetailed(0, uuid, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityCrowdDetailed entityCrowdDetailed =  JSON.parseObject(resultJson,EntityCrowdDetailed.class);
                        if(entityCrowdDetailed.isSuccess()){
                            //成功//showShortToast(R.string.getSuccess);
                            toActivity(CrowdDetailedActivity.createIntent(context, entityCrowdDetailed));

                            progressBarDismiss();
                        }else{//显示失败信息
                            if (entityCrowdDetailed.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityCrowdDetailed.getMessage());
                            }
                        }
                    }else{
                        showShortToast(R.string.noReturn);

                        progressBarDismiss();
                    }
                }
            });
        } else {
            showShortToast(R.string.checkNetwork);
        }
    }

}
