package com.qingye.wtsyou.fragment.home;


import android.app.ProgressDialog;
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
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.adapter.home.StarsChartsAdapter;
import zuo.biao.library.model.EntityBase;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.FocusStars;
import com.qingye.wtsyou.model.RankInfos;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.view.home.StarsChartsView;
import zuo.biao.library.widget.CustomDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeChartsStarsFragment extends BaseHttpRecyclerFragment<RankInfos,StarsChartsView,StarsChartsAdapter> implements CacheCallBack<RankInfos>
        , View.OnClickListener, StarsChartsView.OnItemChildClickListener{

    //名字
    private TextView tvSecName,tvFirName,tvThiName;
    //贡献值
    private TextView tvSecValue,tvFirValue,tvThiValue;
    //关注
    private TextView tvSecFocus,tvFirFocus,tvThiFocus;
    //打榜
    private TextView tvSecRank,tvFirRank,tvThiRank;
    //图片
    private ImageView ivSecImg,ivFirImg,ivThiImg;

    private int currentPage = 1;
    private final int pageSize = 10;
    private int totalPage = 0;
    //是否降序
    private final Boolean desc = false;

    private StarsChartsAdapter starsChartsAdapter;

    private CustomDialog progressBar;

    private List<RankInfos> starsChartsList = new ArrayList<>();
    private List<RankInfos> starsTopChartsList = new ArrayList<>();
    private List<RankInfos> starsOtherChartsList = new ArrayList<>();

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static HomeChartsStarsFragment createInstance() {

        return new HomeChartsStarsFragment();
    }

    public HomeChartsStarsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_home_stars_charts);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        rankingQuery();

        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        initCache(this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        srlBaseHttpRecycler.setEnableRefresh(true);//启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(true);//启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(true);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(true);//尾部

        //srlBaseHttpRecycler.autoRefresh();

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
        //打榜
        tvSecRank = findViewById(R.id.tv_second_rank);
        tvFirRank = findViewById(R.id.tv_first_rank);
        tvThiRank = findViewById(R.id.tv_third_rank);
    }

    public void onResume() {

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //context.unregisterReceiver(mBroadcastReceiver);

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

        setList(new AdapterCallBack<StarsChartsAdapter>() {

            @Override
            public StarsChartsAdapter createAdapter() {
                starsChartsAdapter = new StarsChartsAdapter(context);
                starsChartsAdapter.setOnItemChildClickListener(HomeChartsStarsFragment.this);

                return starsChartsAdapter;
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

        if (starsTopChartsList.get(0) != null) {
            RankInfos first = starsTopChartsList.get(0);
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
                tvFirRank.setVisibility(View.VISIBLE);
            } else {
                tvFirFocus.setVisibility(View.VISIBLE);
                tvFirRank.setVisibility(View.GONE);
            }
        }

        if (starsTopChartsList.get(1) != null) {
            RankInfos second = starsTopChartsList.get(1);
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
                tvSecRank.setVisibility(View.VISIBLE);
            } else {
                tvSecFocus.setVisibility(View.VISIBLE);
                tvSecRank.setVisibility(View.GONE);
            }
        }

        if (starsTopChartsList.get(2) != null) {
            RankInfos third = starsTopChartsList.get(2);
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
                tvThiRank.setVisibility(View.VISIBLE);
            } else {
                tvThiFocus.setVisibility(View.VISIBLE);
                tvThiRank.setVisibility(View.GONE);
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

        tvFirFocus.setOnClickListener(this);
        tvFirRank.setOnClickListener(this);
        tvSecFocus.setOnClickListener(this);
        tvSecRank.setOnClickListener(this);
        tvThiFocus.setOnClickListener(this);
        tvThiRank.setOnClickListener(this);
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

    public void rankingQuery(){
        rankingQuery(1);
    }

    public void rankingQuery(final int page) {
        if (NetUtil.checkNetwork(getActivity())) {
            setProgressBar();
            progressBar.show();

            String keywords = null;
            String periods = null;
            String maxPeriods = null;

            HttpRequest.postStarsRank(0, page, pageSize, desc, keywords, periods, maxPeriods, new OnHttpResponseListener() {

                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                    if(!StringUtil.isEmpty(resultJson)){

                        EntityPageData entityPageData =  JSON.parseObject(resultJson,EntityPageData.class);

                        if(entityPageData.isSuccess()){

                            //成功
                            //showShortToast(R.string.getSuccess);
                            if (page == 1) {
                                currentPage = 1;
                                starsTopChartsList.clear();
                                starsOtherChartsList.clear();
                                starsChartsList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageData.getContent().getData())
                                        ,new TypeToken<List<RankInfos>>(){}.getType());

                                srlBaseHttpRecycler.finishRefresh();
                                srlBaseHttpRecycler.setLoadmoreFinished(false);

                                for (RankInfos starsCharts : starsChartsList) {
                                    if (starsCharts.getRanking() == 1 || starsCharts.getRanking() == 2
                                            || starsCharts.getRanking() == 3) {
                                        starsTopChartsList.add(starsCharts);
                                    } else {
                                        starsOtherChartsList.add(starsCharts);
                                    }
                                }

                                initData();
                            } else {

                                List<RankInfos> starsCharts = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageData.getContent().getData())
                                        ,new TypeToken<List<RankInfos>>(){}.getType());

                                if (starsCharts.size() == 0) {
                                    srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();
                                } else {
                                    starsOtherChartsList.addAll(starsCharts);
                                    srlBaseHttpRecycler.finishLoadmore();
                                }
                            }

                            progressBarDismiss();

                            setList(starsOtherChartsList);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_first_focus:
                if (starsTopChartsList.get(0) != null) {

                    focusStars(starsTopChartsList.get(0),tvFirFocus,tvFirRank);
                }
                break;
            case R.id.tv_first_rank:
                if (starsTopChartsList.get(0) != null) {
                    String firUuid = starsTopChartsList.get(0).getUserId();
                    hit(firUuid,tvFirFocus,tvFirRank);
                }
                break;
            case R.id.tv_second_focus:
                if (starsTopChartsList.get(1) != null) {

                    focusStars(starsTopChartsList.get(1),tvSecFocus,tvSecRank);
                }
                break;
            case R.id.tv_second_rank:
                if (starsTopChartsList.get(1) != null) {
                    String secUuid = starsTopChartsList.get(1).getUserId();
                    hit(secUuid,tvSecFocus,tvSecRank);
                }
                break;
            case R.id.tv_third_focus:
                if (starsTopChartsList.get(2) != null) {

                    focusStars(starsTopChartsList.get(2),tvThiFocus,tvThiRank);
                }
                break;
            case R.id.tv_third_rank:
                if (starsTopChartsList.get(2) != null) {
                    String thiUuid = starsTopChartsList.get(2).getUserId();
                    hit(thiUuid,tvSecFocus,tvSecRank);
                }
                break;
        }
    }

    public void hit(String uuid,final TextView focus,final TextView rank) {

        setProgressBar();
        progressBar.show();

        //检查网络
        if (NetUtil.checkNetwork(getActivity())) {

            HttpRequest.postHit(0, uuid, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  com.alibaba.fastjson.JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功//showShortToast(R.string.getSuccess);
                            showShortToast(R.string.hitSuccess);

                            rankingQuery();

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
            showShortToast(R.string.checkNetwork);
        }

    }

    public void focusStars(RankInfos starsCharts,final TextView focus,final TextView rank) {
        List<FocusStars> focusStarsRequestList = new ArrayList<>();
        FocusStars focusStarsRequest = new FocusStars();
        focusStarsRequest.setStarUuid(starsCharts.getUserId());
        focusStarsRequest.setStarName(starsCharts.getUserName());
        focusStarsRequest.setStarPhoto(starsCharts.getUserPhoto());
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
                            rank.setVisibility(View.VISIBLE);

                            rankingQuery();

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

    //点击item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //toActivity(StarsMainActivity.createIntent(context, id));
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
        focusStars(starsOtherChartsList.get(position),focus,rank);
    }

    @Override
    public void onHitClick(View view, int position, TextView focus, TextView rank) {
        String secUuid = starsOtherChartsList.get(position).getUserId();
        hit(secUuid,focus,rank);
    }
}
