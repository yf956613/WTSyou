package com.qingye.wtsyou.activity.search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.fragment.campaign.SearchCampaignCrowdFragment;
import com.qingye.wtsyou.fragment.campaign.SearchCampaignShowFragment;
import com.qingye.wtsyou.fragment.campaign.SearchCampaignSupportFragment;
import com.qingye.wtsyou.fragment.campaign.SearchCampaignVoteFragment;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.Campaign;
import com.qingye.wtsyou.model.Concert;
import com.qingye.wtsyou.model.Crowd;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.Supports;
import com.qingye.wtsyou.model.Vote;
import com.qingye.wtsyou.utils.CampaignUtil;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class SearchCampaignActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private EditText edtContent;
    private RelativeLayout rlCancel;
    private LinearLayout llVote,llCrowd,llShow,llSupport;

    private List<Campaign> campaignList = new ArrayList<>();
    private List<Vote> voteList = new ArrayList<>();
    private List<Crowd> crowdList = new ArrayList<>();
    private List<Concert> concertList = new ArrayList<>();
    private List<Supports> supportsList = new ArrayList<>();

    private HttpModel<EntityPageData> mEntityPageDataHttpModel;

    private CustomDialog progressBar;
    private ScrollView scrollView;
    private SwipeRefreshLayout swipeRefresh;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, SearchCampaignActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_campaign,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //明星列表
        mEntityPageDataHttpModel = new HttpModel<>(EntityPageData.class);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        swipeRefresh = findView(R.id.swipe_refresh_widget);
        scrollView = findView(R.id.scrollview);

        edtContent = findView(R.id.edit_search_content);
        edtContent.setHint("搜索活动");
        rlCancel = findView(R.id.rl_cancel);

        edtContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_SEND
                        || keyCode == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    // do some your things
                    if (edtContent.getText().toString().trim().isEmpty()) {
                        showShortToast(R.string.noContent);
                    } else {
                        searchCampaign(edtContent.getText().toString().trim());
                    }
                }
                return false;
            }
        });

        llVote = findView(R.id.llVote);
        llCrowd = findView(R.id.llCrowd);
        llShow = findView(R.id.llShow);
        llSupport = findView(R.id.llSupport);
    }

    //刷新
    private void initHrvsr(){
        //设置刷新时动画的颜色，可以设置4个
        swipeRefresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefresh.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("swipeRefresh", "invoke onRefresh...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        searchCampaign(edtContent.getText().toString().trim());
                        showShortToast(R.string.getSuccess);
                        swipeRefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });
        // 设置子视图是否允许滚动到顶部
        swipeRefresh.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                return scrollView.getScrollY() > 0;
            }
        });
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
    public void initData() {

    }

    @Override
    public void initEvent() {
        rlCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    public void analysisList() {
        for (Campaign campaign : campaignList) {
            if (campaign.getActivityProperty().equals("vote")) {
                voteList.add(CampaignUtil.toVote(campaign));

                if (voteList.size() > 0) {
                    llVote.setVisibility(View.VISIBLE);
                } else {
                    llVote.setVisibility(View.GONE);
                }
            }
            else if (campaign.getActivityProperty().equals("crowd")) {
                crowdList.add(CampaignUtil.toCrowd(campaign));

                if (crowdList.size() > 0) {
                    llCrowd.setVisibility(View.VISIBLE);
                } else {
                    llCrowd.setVisibility(View.GONE);
                }
            }
            else if (campaign.getActivityProperty().equals("concert")) {
                concertList.add(CampaignUtil.toConcert(campaign));

                if (concertList.size() > 0) {
                    llShow.setVisibility(View.VISIBLE);
                } else {
                    llShow.setVisibility(View.GONE);
                }
            }
            else if (campaign.getActivityProperty().equals("support")) {
                supportsList.add(CampaignUtil.toSupport(campaign));

                if (supportsList.size() > 0) {
                    llSupport.setVisibility(View.VISIBLE);
                } else {
                    llSupport.setVisibility(View.GONE);
                }
            }
        }

        fragment();
    }

    public void fragment() {
        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        SearchCampaignVoteFragment searchCampaignVoteFragment = new SearchCampaignVoteFragment();
        if (voteList.size() > 0) {
            Bundle bundleVote = new Bundle();
            bundleVote.putSerializable(Constant.VOTELIST, (Serializable) voteList);
            searchCampaignVoteFragment.setArguments(bundleVote);
            transaction.replace(R.id.list_vote,searchCampaignVoteFragment);
        }
        SearchCampaignCrowdFragment searchCampaignCrowdFragment = new SearchCampaignCrowdFragment();
        if (crowdList.size() > 0) {
            Bundle bundleCrowd = new Bundle();
            bundleCrowd.putSerializable(Constant.CROWDLIST, (Serializable) crowdList);
            searchCampaignCrowdFragment.setArguments(bundleCrowd);
            transaction.replace(R.id.list_crowd,searchCampaignCrowdFragment);
        }
        SearchCampaignShowFragment searchCampaignShowFragment = new SearchCampaignShowFragment();
        if (concertList.size() > 0) {
            Bundle bundleConcert = new Bundle();
            bundleConcert.putSerializable(Constant.SHOWLIST, (Serializable) concertList);
            searchCampaignShowFragment.setArguments(bundleConcert);
            transaction.replace(R.id.list_show,searchCampaignShowFragment);
        }
        SearchCampaignSupportFragment searchCampaignSupportFragment = new SearchCampaignSupportFragment();
        if (supportsList.size() > 0) {
            Bundle bundleSupport = new Bundle();
            bundleSupport.putSerializable(Constant.SUPPORTLIST, (Serializable) supportsList);
            searchCampaignSupportFragment.setArguments(bundleSupport);
            transaction.replace(R.id.list_support,searchCampaignSupportFragment);
        }

        transaction.commit();
    }

    public void searchCampaign(String keyWords) {
        setProgressBar();
        progressBar.show();

        String request = HttpRequest.postCampaignSearch(keyWords);
        //返回列表
        mEntityPageDataHttpModel.post(request, URL_BASE + URLConstant.SEARCHCAMPAIGN,1,this);
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public void Success(String url, int RequestCode, EntityBase entityBase) {
        super.Success(url, RequestCode, entityBase);
        switch (RequestCode) {
            case 1:
                EntityPageData pageData = mEntityPageDataHttpModel.getData();
                campaignList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(pageData.getContent().getData())
                        ,new TypeToken<List<Campaign>>(){}.getType());
                analysisList();

                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }

}
