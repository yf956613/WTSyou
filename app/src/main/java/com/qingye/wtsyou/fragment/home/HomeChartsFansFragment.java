package com.qingye.wtsyou.fragment.home;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.home.FansMainActivity;
import com.qingye.wtsyou.adapter.home.FansChartsAdapter;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.RankInfos;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.view.home.FansChartsView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.widget.CustomDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeChartsFansFragment extends BaseHttpRecyclerFragment<RankInfos,FansChartsView,FansChartsAdapter> implements CacheCallBack<RankInfos>
        , View.OnClickListener, FansChartsView.OnItemChildClickListener {

    //名字
    private TextView tvSecName,tvFirName,tvThiName;
    //贡献值
    private TextView tvSecValue,tvFirValue,tvThiValue;
    //关注
    private TextView tvSecFocus,tvFirFocus,tvThiFocus;
    //取消
    private TextView tvSecCan,tvFirCan,tvThiCan;
    //图片
    private ImageView ivSecImg,ivFirImg,ivThiImg;

    private int currentPage = 1;
    private final int pageSize = 10;
    private int totalPage = 0;
    //是否降序
    private final Boolean desc = false;

    private CustomDialog progressBar;

    private FansChartsAdapter fansChartsAdapter;

    private List<RankInfos> fansChartsList = new ArrayList<>();
    private List<RankInfos> fansTopChartsList = new ArrayList<>();
    private List<RankInfos> fansOtherChartsList = new ArrayList<>();

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static HomeChartsFansFragment createInstance() {

        return new HomeChartsFansFragment();
    }

    public HomeChartsFansFragment() {
        // Required empty public constructor
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastAction.ACTION_RANKING_FANS_REFRESH)) {
                rankingQuery();
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_home_fans_charts);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        rankingQuery();

        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        srlBaseHttpRecycler.autoRefresh();
        /*srlBaseHttpRecycler.setEnableRefresh(true);//启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(true);//启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(true);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(true);//尾部*/

        return view;
    }

    @Override
    public void initView() {
        super.initView();

        //名字
        tvSecName = findViewById(R.id.tv_second_name);
        tvFirName = findViewById(R.id.tv_first_name);
        tvThiName = findViewById(R.id.tv_third_name);
        //贡献值
        tvSecValue = findViewById(R.id.tv_second_value);
        tvFirValue = findViewById(R.id.tv_first_value);
        tvThiValue = findViewById(R.id.tv_third_value);
        //图片
        ivSecImg = findViewById(R.id.iv_second_img);
        ivFirImg = findViewById(R.id.iv_first_img);
        ivThiImg = findViewById(R.id.iv_third_img);
        //关注
        tvSecFocus = findViewById(R.id.tv_second_focus);
        tvFirFocus = findViewById(R.id.tv_first_focus);
        tvThiFocus = findViewById(R.id.tv_third_focus);
        //取消关注
        tvSecCan = findViewById(R.id.tv_second_cancel);
        tvFirCan = findViewById(R.id.tv_first_cancel);
        tvThiCan = findViewById(R.id.tv_third_cancel);
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
    public void setList(final List<RankInfos> list) {

        setList(new AdapterCallBack<FansChartsAdapter>() {

            @Override
            public FansChartsAdapter createAdapter() {
                fansChartsAdapter = new FansChartsAdapter(context);
                fansChartsAdapter.setOnItemChildClickListener(HomeChartsFansFragment.this);

                return fansChartsAdapter;
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

    @Override
    public void initData() {
        super.initData();

        if (fansTopChartsList.size() == 0) {
            return;
        }

        if (fansTopChartsList.get(0) != null) {
            RankInfos first = fansTopChartsList.get(0);
            //名字
            tvFirName.setText(first.getUserName());
            //贡献
            tvFirValue.setText("" + first.getScore());
            //图片
            String url1 = first.getUserPhoto();
            if (url1 != null) {
                Glide.with(context)
                        .load(url1)
                        .into(ivFirImg);
            } else {
                int defaultHead = R.mipmap.head;
                Glide.with(context)
                        .load(defaultHead)
                        .into(ivFirImg);
            }
            if (first.getFollow()) {
                tvFirFocus.setVisibility(View.GONE);
                tvFirCan.setVisibility(View.VISIBLE);
            } else {
                tvFirFocus.setVisibility(View.VISIBLE);
                tvFirCan.setVisibility(View.GONE);
            }

        }

        if (fansTopChartsList.size() < 2) {
            return;
        }

        if (fansTopChartsList.get(1) != null) {
            RankInfos second = fansTopChartsList.get(1);
            //名字
            tvSecName.setText(second.getUserName());
            //贡献
            tvSecValue.setText("" + second.getScore());
            //图片
            String url2 = second.getUserPhoto();
            if (url2 != null) {
                Glide.with(context)
                        .load(url2)
                        .into(ivSecImg);
            } else {
                int defaultHead = R.mipmap.head;
                Glide.with(context)
                        .load(defaultHead)
                        .into(ivSecImg);
            }
            if (second.getFollow()) {
                tvSecFocus.setVisibility(View.GONE);
                tvSecCan.setVisibility(View.VISIBLE);
            } else {
                tvSecFocus.setVisibility(View.VISIBLE);
                tvSecCan.setVisibility(View.GONE);
            }
        }

        if (fansTopChartsList.size() < 2) {
            return;
        }

        if (fansTopChartsList.get(2) != null) {
            RankInfos third = fansTopChartsList.get(2);
            //名字
            tvThiName.setText(third.getUserName());
            //贡献
            tvThiValue.setText("" + third.getScore());
            //图片
            String url3 = third.getUserPhoto();
            if (url3 != null) {
                Glide.with(context)
                        .load(url3)
                        .into(ivThiImg);
            } else {
                int defaultHead = R.mipmap.head;
                Glide.with(context)
                        .load(defaultHead)
                        .into(ivThiImg);
            }
            if (third.getFollow()) {
                tvThiFocus.setVisibility(View.GONE);
                tvThiCan.setVisibility(View.VISIBLE);
            } else {
                tvThiFocus.setVisibility(View.VISIBLE);
                tvThiCan.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public List<RankInfos> parseArray(String json) {
        return null;
    }

    @Override
    public Class<RankInfos> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(RankInfos data) {
        return null;
    }

    @Override
    public int getCacheCount() {
        return 10;
    }

    @Override
    public void initEvent() {
        super.initEvent();

        tvFirFocus.setOnClickListener(this);
        tvFirCan.setOnClickListener(this);
        tvSecFocus.setOnClickListener(this);
        tvSecCan.setOnClickListener(this);
        tvThiFocus.setOnClickListener(this);
        tvThiCan.setOnClickListener(this);

        ivFirImg.setOnClickListener(this);
        ivSecImg.setOnClickListener(this);
        ivThiImg.setOnClickListener(this);
    }

    public void rankingQuery(){
        rankingQuery(1);
    }

    public void rankingQuery(final int page) {
        if (NetUtil.checkNetwork(getActivity())) {

            String keywords = null;
            String periods = null;
            String maxPeriods = null;

            HttpRequest.postFansRank(0, page, pageSize, desc, keywords, periods, maxPeriods, new OnHttpResponseListener() {

                        @Override
                        public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                            if(!StringUtil.isEmpty(resultJson)){

                                EntityPageData entityPageData =  JSON.parseObject(resultJson,EntityPageData.class);

                                if(entityPageData.isSuccess()){
                                    //成功
                                    //showShortToast(R.string.getSuccess);
                                    if (page == 1) {
                                        currentPage = 1;
                                        fansTopChartsList.clear();
                                        fansOtherChartsList.clear();
                                        fansChartsList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageData.getContent().getData())
                                                ,new TypeToken<List<RankInfos>>(){}.getType());

                                        srlBaseHttpRecycler.finishRefresh();
                                        srlBaseHttpRecycler.setLoadmoreFinished(false);

                                        for (RankInfos fansCharts : fansChartsList) {
                                            if (fansCharts.getRanking() == 1 || fansCharts.getRanking() == 2
                                                    || fansCharts.getRanking() == 3) {
                                                fansTopChartsList.add(fansCharts);
                                            } else {
                                                fansOtherChartsList.add(fansCharts);
                                            }
                                        }

                                        initData();
                                    } else {

                                        List<RankInfos> fansCharts = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageData.getContent().getData())
                                                ,new TypeToken<List<RankInfos>>(){}.getType());

                                        if (fansCharts.size() == 0) {
                                            srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();
                                        } else {
                                            fansOtherChartsList.addAll(fansCharts);
                                            srlBaseHttpRecycler.finishLoadmore();
                                        }
                                    }

                                    setList(fansOtherChartsList);

                                    totalPage = entityPageData.getContent().getPageCount();

                                    currentPage ++;

                                }else{//显示失败信息

                                    showShortToast(entityPageData.getMessage());
                                }

                            }else{
                                showShortToast(R.string.noReturn);
                            }
                        }
                    });
        } else {
            showShortToast(R.string.checkNetwork);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_first_focus:
                if (fansTopChartsList.get(0) != null) {

                    focusFans(fansTopChartsList.get(0),tvFirFocus,tvFirCan);
                }
                break;
            case R.id.tv_first_cancel:
                if (fansTopChartsList.get(0) != null) {
                    cancelFans(fansTopChartsList.get(0),tvFirFocus,tvFirCan);

                }
                break;
            case R.id.tv_second_focus:
                if (fansTopChartsList.get(1) != null) {

                    focusFans(fansTopChartsList.get(1),tvSecFocus,tvSecCan);
                }
                break;
            case R.id.tv_second_cancel:
                if (fansTopChartsList.get(1) != null) {
                    cancelFans(fansTopChartsList.get(1),tvSecFocus,tvSecCan);

                }
                break;
            case R.id.tv_third_focus:
                if (fansTopChartsList.get(2) != null) {

                    focusFans(fansTopChartsList.get(2),tvThiFocus,tvThiCan);
                }
                break;
            case R.id.tv_third_cancel:
                if (fansTopChartsList.get(2) != null) {
                    cancelFans(fansTopChartsList.get(2),tvThiFocus,tvThiCan);

                }
                break;
            case R.id.iv_second_img:
                toActivity(FansMainActivity.createIntent(context, fansTopChartsList.get(1).getUserId()));
                break;
            case R.id.iv_first_img:
                toActivity(FansMainActivity.createIntent(context, fansTopChartsList.get(0).getUserId()));
                break;
            case R.id.iv_third_img:
                toActivity(FansMainActivity.createIntent(context, fansTopChartsList.get(2).getUserId()));
                break;
            default:
                break;
        }
    }

    public void focusFans(RankInfos fansCharts, final TextView focus, final TextView can) {

        if (NetUtil.checkNetwork(getActivity())) {
            setProgressBar();
            progressBar.show();

            HttpRequest.getGetFocusFriend(0, fansCharts.getUserId(), new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  zuo.biao.library.util.JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功
                            showShortToast(R.string.focusSuccess);

                            focus.setVisibility(View.GONE);
                            can.setVisibility(View.VISIBLE);

                            rankingQuery();

                            progressBarDismiss();

                        }else{//显示失败信息

                            showShortToast(entityBase.getMessage());
                            progressBarDismiss();
                        }
                    }else{
                        showShortToast(R.string.noReturn);

                        progressBarDismiss();
                    }
                }
            });

        } else {
            showProgressDialog(R.string.checkNetwork);
        }
    }

    public void cancelFans(RankInfos fansCharts, final TextView focus, final TextView can) {

        if (NetUtil.checkNetwork(context)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.getGetCancelFocusFriend(0, fansCharts.getUserId(), new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  zuo.biao.library.util.JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功
                            showShortToast(R.string.cancelFocusSuccess);

                            focus.setVisibility(View.VISIBLE);
                            can.setVisibility(View.GONE);

                            progressBarDismiss();

                        }else{//显示失败信息

                            showShortToast(entityBase.getMessage());
                            progressBarDismiss();
                        }
                    }else{
                        showShortToast(R.string.noReturn);

                        progressBarDismiss();
                    }
                }
            });

        } else {
            showProgressDialog(R.string.checkNetwork);
        }
    }

    //点击item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toActivity(FansMainActivity.createIntent(context, fansOtherChartsList.get(position).getUserId()));
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        rankingQuery();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        rankingQuery(currentPage);
    }

    @Override
    public void onFocus(View view, int position, TextView focus, TextView rank) {
        focusFans(fansOtherChartsList.get(position),focus,rank);
    }

    @Override
    public void onCancelFocus(View view, int position, TextView focus, TextView rank) {
        cancelFans(fansOtherChartsList.get(position),focus,rank);
    }

}
