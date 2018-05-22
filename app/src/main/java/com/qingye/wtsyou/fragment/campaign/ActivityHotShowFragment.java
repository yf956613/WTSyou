package com.qingye.wtsyou.fragment.campaign;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.qingye.wtsyou.activity.campaign.VoteDetailedActivity;
import com.qingye.wtsyou.adapter.campaign.ActivityHotShowAdapter;
import com.qingye.wtsyou.modle.EntityCampaignHome;
import com.qingye.wtsyou.modle.EntityCrowdDetailed;
import com.qingye.wtsyou.modle.EntitySaleDetailed;
import com.qingye.wtsyou.modle.EntityVoteDetailed;
import com.qingye.wtsyou.modle.Hots;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.view.campaign.ActivityHotShowView;
import com.qingye.wtsyou.widget.CustomDialog;

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

        //检查网络
        if (NetUtil.checkNetwork(context)) {
            String activityState = hotCampaigns.get(position).getActivityState();
            String uuid = hotCampaigns.get(position).getActivityId();

            setProgressBar();
            progressBar.show();

            if (activityState.equals("voting") || activityState.equals("votesuccess") || activityState.equals("votefail")) {
                HttpRequest.getVoteDetailed(0, uuid, new OnHttpResponseListener() {
                    @Override
                    public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                        if(!StringUtil.isEmpty(resultJson)){
                            EntityVoteDetailed entityVoteDetailed =  JSON.parseObject(resultJson,EntityVoteDetailed.class);
                            if(entityVoteDetailed.isSuccess()){
                                //成功//showShortToast(R.string.getSuccess);
                                toActivity(VoteDetailedActivity.createIntent(context, entityVoteDetailed));

                                progressBarDismiss();
                            }else{//显示失败信息
                                if (entityVoteDetailed.getCode().equals("401")) {
                                    showShortToast(R.string.tokenInvalid);
                                    toActivity(MainActivity.createIntent(context));
                                } else {
                                    showShortToast(entityVoteDetailed.getMessage());
                                }

                                progressBarDismiss();
                            }
                        }else{
                            showShortToast(R.string.noReturn);

                            progressBarDismiss();
                        }
                    }
                });
            }
            else if (activityState.equals("crowding") || activityState.equals("crowdsuccess") || activityState.equals("crowdfail")) {
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

                                progressBarDismiss();
                            }
                        }else{
                            showShortToast(R.string.noReturn);

                            progressBarDismiss();
                        }
                    }
                });
            }
            else if (activityState.equals("saling")) {
                HttpRequest.getSaleDetailed(0, uuid, new OnHttpResponseListener() {
                    @Override
                    public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                        if(!StringUtil.isEmpty(resultJson)){
                            EntitySaleDetailed entitySaleDetailed =  JSON.parseObject(resultJson,EntitySaleDetailed.class);
                            if(entitySaleDetailed.isSuccess()){
                                //成功//showShortToast(R.string.getSuccess);
                                toActivity(SaleDetailedActivity.createIntent(context,entitySaleDetailed));

                                progressBarDismiss();
                            }else{//显示失败信息
                                if (entitySaleDetailed.getCode().equals("401")) {
                                    showShortToast(R.string.tokenInvalid);
                                    toActivity(MainActivity.createIntent(context));
                                } else {
                                    showShortToast(entitySaleDetailed.getMessage());
                                }

                                progressBarDismiss();
                            }
                        }else{
                            showShortToast(R.string.noReturn);

                            progressBarDismiss();
                        }
                    }
                });
            }
        } else {
            showShortToast(R.string.checkNetwork);
        }

    }
}
