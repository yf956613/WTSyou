package com.qingye.wtsyou.fragment.campaign;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.campaign.CreateSupportActivity;
import com.qingye.wtsyou.activity.campaign.CreateVoteActivity;
import com.qingye.wtsyou.activity.search.SearchCampaignActivity;
import com.qingye.wtsyou.activity.campaign.ShowAllActivity;
import com.qingye.wtsyou.activity.campaign.SupportAllActivity;
import com.qingye.wtsyou.adapter.campaign.LoopShowContentAdapter;
import com.qingye.wtsyou.modle.Banners;
import com.qingye.wtsyou.modle.Embeds;
import com.qingye.wtsyou.modle.EntityCampaignHome;
import com.qingye.wtsyou.modle.Hots;
import com.qingye.wtsyou.modle.Officials;
import com.qingye.wtsyou.modle.Supports;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.widget.CustomDialog;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import wzp.demo.imageloop.widget.ImageLoopViewPager;
import wzp.demo.imageloop.widget.PageIndicatorView;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {
    //轮播图
    private RelativeLayout relativeLoopImage;
    private ImageLoopViewPager vpNews;
    private PageIndicatorView pageIndicatorView;
    //广告
    private LinearLayout llAd;
    private ImageView ivAd;
    private TextView tvAdName;

    private ImageView ivCreate;
    private Dialog dialog;//发起活动用全屏dialog展示
    private LinearLayout llSearch;
    private LinearLayout llShowMore,llSupportMore;

    private List<Banners> bannersList = new ArrayList<>();
    private List<Officials> officialsList = new ArrayList<>();
    private List<Hots> hotCampaignList = new ArrayList<>();
    private List<Embeds> embedsList = new ArrayList<>();
    private List<Supports> supportsList = new ArrayList<>();

    private List<View> viewList = new ArrayList<>();
    private LoopShowContentAdapter loopAdapter;

    private ScrollView scrollView;
    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_activity);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        getCampaignHome();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        return view;
    }

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static ActivityFragment createInstance() {
        return new ActivityFragment();
    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }

    @Override
    public void initView() {
        scrollView = findViewById(R.id.scrollview);
        swipeRefresh = findViewById(R.id.swipe_refresh_widget);

        //轮播图
        relativeLoopImage = findViewById(R.id.relative_loopImage);
        vpNews = findViewById(R.id.vp_news);
        pageIndicatorView = findViewById(R.id.pageIndicatorView);
        //广告
        llAd = findViewById(R.id.ll_ad);
        ivAd = findViewById(R.id.iv_ad_img);
        tvAdName = findViewById(R.id.tv_ad_name);

        ivCreate = findView(R.id.iv_create);
        llSearch = findView(R.id.ll_search);
        llShowMore = findViewById(R.id.ll_show_more);
        llSupportMore = findViewById(R.id.ll_support_more);
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
                        getCampaignHome();

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
    public void initData() {

    }

    @Override
    public void initEvent() {
        ivCreate.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        llShowMore.setOnClickListener(this);
        llSupportMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_create:
                //创建对话框
                dialog = new Dialog(getContext(),R.style.dialog1);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.support_or_vote,null);
                //给Dialog中的子view设置事件监听
                view.findViewById(R.id.ll_support).setOnClickListener(this);
                view.findViewById(R.id.ll_vote).setOnClickListener(this);
                view.findViewById(R.id.rl_close).setOnClickListener(this);
                dialog.setContentView(view);
                //自定义宽高（高度一般不用调整，在xml调整好就可以了，这里我只调整了宽度）
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(params);
                //show之前设置返回键无效，触摸屏无效
                dialog.setCancelable(false);
                //显示对话框
                dialog.show();
                break;
            case R.id.ll_support:
                toActivity(CreateSupportActivity.createIntent(context));
                dialog.dismiss();
                break;
            case R.id.ll_vote:
                toActivity(CreateVoteActivity.createIntent(context));
                dialog.dismiss();
                break;
            case R.id.rl_close:
                dialog.dismiss();
                break;
            case R.id.ll_search:
                toActivity(SearchCampaignActivity.createIntent(context));
                break;
            case R.id.ll_show_more:
                toActivity(ShowAllActivity.createIntent(context));
                break;
            case R.id.ll_support_more:
                toActivity(SupportAllActivity.createIntent(context));
                break;
            default:
                break;
        }
    }

    private void getCampaignHome() {

        //检查网络
        if (NetUtil.checkNetwork(context)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.postCampaignHome(0, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityCampaignHome entityCampaignHome =  JSON.parseObject(resultJson,EntityCampaignHome.class);

                        if(entityCampaignHome.isSuccess()){
                            //成功
                            //showShortToast(R.string.getSuccess);
                            //轮播图
                            bannersList.clear();
                            bannersList = entityCampaignHome.getContent().getBanners();
                            //官方活动
                            officialsList = entityCampaignHome.getContent().getOfficials();
                            //热门活动
                            hotCampaignList = entityCampaignHome.getContent().getHots();
                            //广告
                            embedsList = entityCampaignHome.getContent().getEmbeds();
                            //应援
                            supportsList = entityCampaignHome.getContent().getSupports();
                            //轮播图
                            imageloop();
                            //广告
                            ad();
                            //其他
                            fragment();

                        }else{//显示失败信息
                            if (entityCampaignHome.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityCampaignHome.getMessage());
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

    //轮播图
    private void imageloop(){
        viewList.clear();
        if (bannersList.size() > 0) {
            for (Banners banner : bannersList) {
                if (banner.getState().equals("disable")) {
                    bannersList.remove(banner);
                }
            }
            //图片轮播改变ui需要开启新线程
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < bannersList.size(); i++) {
                        View view = getLayoutInflater().inflate(R.layout.loop_show, null);
                        //final int j = i;
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {//点击事件

                            }
                        });
                        viewList.add(view);
                    }
                    pageIndicatorView.setCount(viewList.size());
                    vpNews.setPage(viewList.size());

                    loopAdapter = new LoopShowContentAdapter(context, viewList, bannersList);
                    vpNews.setAdapter(loopAdapter);
                }
            });
        }
    }

    //内嵌广告
    private void ad() {
        if (embedsList.size() > 0) {
            llAd.setVisibility(View.VISIBLE);

            String url = embedsList.get(0).getPicUrl();

            Glide.with(context)
                    .load(url)
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(ivAd);

            tvAdName.setText(embedsList.get(0).getTitle());
        } else {
            llAd.setVisibility(View.GONE);
        }
    }

    //其他信息
    private void fragment() {
        //注意这里是调用getChildFragmentManager()方法
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        ActivityOfficialFragment activityOfficialFragment = new ActivityOfficialFragment();
        if (officialsList.size() > 0) {
            Bundle bundleOfficial = new Bundle();
            bundleOfficial.putSerializable(Constant.OFFICIALS, (Serializable) officialsList);
            activityOfficialFragment.setArguments(bundleOfficial);
            transaction.replace(R.id.list_official,activityOfficialFragment);
        }

        ActivityHotShowFragment activityHotShowFragment = new ActivityHotShowFragment();
        if (hotCampaignList.size() > 0) {
            final List<Hots> enabled = new ArrayList<>();
            for (Hots hots : hotCampaignList) {
                if (hots.getState().equals("enabled")) {
                    enabled.add(hots);
                }
            }
            Bundle bundleHot = new Bundle();
            bundleHot.putSerializable(Constant.HOTCAMPAIGN, (Serializable) enabled);
            activityHotShowFragment.setArguments(bundleHot);
            transaction.replace(R.id.list_show,activityHotShowFragment);
        }

        ActivitySupportFragment activitySupportFragment = new ActivitySupportFragment();
        if (supportsList.size() > 0) {
            List<Supports> supports = new ArrayList<>();
            if (supportsList.size() == 1) {
                supports.add(supportsList.get(0));
            } else {
                supports.add(supportsList.get(0));
                supports.add(supportsList.get(1));
            }

            Bundle bundleSupport = new Bundle();
            bundleSupport.putSerializable(Constant.SUPPORT, (Serializable) supports);
            activitySupportFragment.setArguments(bundleSupport);
            transaction.replace(R.id.list_support,activitySupportFragment);
        }

        transaction.commit();

        progressBarDismiss();
    }
}
