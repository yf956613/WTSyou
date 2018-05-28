package com.qingye.wtsyou.activity.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.adapter.home.FansChartsAdapter;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.model.EntityBase;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.FocusStars;
import com.qingye.wtsyou.model.RankInfos;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.DataUtil;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.view.home.FansChartsView;
import zuo.biao.library.widget.CustomDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class PastFansChartsDetailedActivity extends BaseHttpRecyclerActivity<RankInfos,FansChartsView,FansChartsAdapter> implements View.OnClickListener,
        View.OnLongClickListener, OnBottomDragListener, FansChartsView.OnItemChildClickListener {

    private ImageView ivBack,ivRules;
    private TextView tvHead;

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
    //关键
    private String keywords;
    private String periodsZone;

    private CustomDialog progressBar;

    private FansChartsAdapter fansChartsAdapter;

    private List<RankInfos> fansChartsList = new ArrayList<>();
    private List<RankInfos> fansTopChartsList = new ArrayList<>();
    private List<RankInfos> fansOtherChartsList = new ArrayList<>();


    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, String periods, String periodsZone) {
        return new Intent(context,PastFansChartsDetailedActivity.class).putExtra(Constant.PERIODS, periods).putExtra(Constant.PERIODSZONE, periodsZone);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_fans_charts_detailed,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        intent = getIntent();
        keywords = intent.getStringExtra(Constant.PERIODS);
        periodsZone = intent.getStringExtra(Constant.PERIODSZONE);

        rankingQuery();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        /*//禁止滑动
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);
        linearLayoutManager.setScrollEnabled(false);
        rvBaseRecycler.setNestedScrollingEnabled(false);//解决卡顿
        rvBaseRecycler.setLayoutManager(linearLayoutManager);*/

        //srlBaseHttpRecycler.autoRefresh();
        srlBaseHttpRecycler.setEnableRefresh(true);//启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(true);//启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(true);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(true);//尾部
    }

    @Override
    public void initView() {
        super.initView();
        ivBack = findViewById(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        ivRules = findViewById(R.id.iv_right);
        ivRules.setImageResource(R.mipmap.rule);
        tvHead = findView(R.id.tv_head_title);

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
                fansChartsAdapter.setOnItemChildClickListener(PastFansChartsDetailedActivity.this);

                return fansChartsAdapter;
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
            }
            if (first.getFollow()) {
                tvFirFocus.setVisibility(View.GONE);
                tvFirCan.setVisibility(View.VISIBLE);
            } else {
                tvFirFocus.setVisibility(View.VISIBLE);
                tvFirCan.setVisibility(View.GONE);
            }

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
            }
            if (second.getFollow()) {
                tvSecFocus.setVisibility(View.GONE);
                tvSecCan.setVisibility(View.VISIBLE);
            } else {
                tvSecFocus.setVisibility(View.VISIBLE);
                tvSecCan.setVisibility(View.GONE);
            }
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
    public void getListAsync(int page) {

    }

    @Override
    public List<RankInfos> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
        ivRules.setOnClickListener(this);

        tvFirFocus.setOnClickListener(this);
        tvFirCan.setOnClickListener(this);
        tvSecFocus.setOnClickListener(this);
        tvSecCan.setOnClickListener(this);
        tvThiFocus.setOnClickListener(this);
        tvThiCan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                toActivity(PastChartsRuleActivity.createIntent(context));
                break;
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
            default:
                break;
        }
    }

    public void rankingQuery(){
        rankingQuery(1);
    }

    public void rankingQuery(final int page) {
        if (NetUtil.checkNetwork(getActivity())) {
            setProgressBar();
            progressBar.show();

            String periods = null;
            String maxPeriods = null;

            HttpRequest.postHistoryFansRank(0, page, pageSize, desc, keywords, periods, maxPeriods, new OnHttpResponseListener() {

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

                                for (RankInfos starsCharts : fansChartsList) {
                                    if (starsCharts.getRanking() == 1 || starsCharts.getRanking() == 2
                                            || starsCharts.getRanking() == 3) {
                                        fansTopChartsList.add(starsCharts);
                                    } else {
                                        fansOtherChartsList.add(starsCharts);
                                    }
                                }

                                initData();
                            } else {

                                List<RankInfos> starsCharts = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageData.getContent().getData())
                                        ,new TypeToken<List<RankInfos>>(){}.getType());

                                if (starsCharts.size() == 0) {
                                    srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();
                                } else {
                                    fansOtherChartsList.addAll(starsCharts);
                                    srlBaseHttpRecycler.finishLoadmore();
                                }
                            }

                            progressBarDismiss();

                            setList(fansOtherChartsList);

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

    public void focusFans(RankInfos fansCharts, final TextView focus, final TextView can) {
        List<FocusStars> focusStarsRequestList = new ArrayList<>();
        FocusStars focusStarsRequest = new FocusStars();
        focusStarsRequest.setStarUuid(fansCharts.getUserId());
        focusStarsRequest.setStarName(fansCharts.getUserName());
        focusStarsRequest.setStarPhoto(fansCharts.getUserPhoto());
        focusStarsRequestList.add(focusStarsRequest);

        if (NetUtil.checkNetwork(getActivity())) {
            setProgressBar();
            progressBar.show();

            HttpRequest.postFocusStars(0, focusStarsRequestList, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  zuo.biao.library.util.JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功
                            showShortToast(R.string.focusSuccess);

                            focus.setVisibility(View.GONE);
                            can.setVisibility(View.VISIBLE);

                            Intent intent = new Intent(
                                    BroadcastAction.ACTION_RANKING_FANS_REFRESH);
                            // 发送广播
                            context.sendBroadcast(intent);

                            progressBarDismiss();

                        }else{//显示失败信息
                            if (entityBase.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityBase.getMessage());
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
            showProgressDialog(R.string.checkNetwork);
        }
    }

    public void cancelFans(RankInfos fansCharts, final TextView focus, final TextView can) {

        List<FocusStars> cancelStarsRequest = new ArrayList<>();
        FocusStars focusStarsRequest = new FocusStars();
        focusStarsRequest.setStarUuid(fansCharts.getUserId());
        focusStarsRequest.setStarName(fansCharts.getUserName());
        focusStarsRequest.setStarPhoto(fansCharts.getUserPhoto());
        cancelStarsRequest.add(focusStarsRequest);

        if (NetUtil.checkNetwork(context)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.postCancelStars(0, cancelStarsRequest, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  zuo.biao.library.util.JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功
                            showShortToast(R.string.cancelFocusSuccess);

                            focus.setVisibility(View.VISIBLE);
                            can.setVisibility(View.GONE);


                            Intent intent = new Intent(
                                    BroadcastAction.ACTION_RANKING_FANS_REFRESH);
                            // 发送广播
                            context.sendBroadcast(intent);

                            progressBarDismiss();

                        }else{//显示失败信息
                            if (entityBase.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityBase.getMessage());
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
            showProgressDialog(R.string.checkNetwork);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onDragBottom(boolean rightToLeft) {
        finish();
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
