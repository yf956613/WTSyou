package com.qingye.wtsyou.fragment.fragmentFactory;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.home.RecommendStarsConversationActivity;
import com.qingye.wtsyou.activity.search.SearchFansActivity;
import com.qingye.wtsyou.adapter.home.HomeContentAdapter;
import com.qingye.wtsyou.basemodel.EntityBase;
import com.qingye.wtsyou.modle.EntityHomeContent;
import com.qingye.wtsyou.modle.EntityHomeList;
import com.qingye.wtsyou.modle.EntityPageData;
import com.qingye.wtsyou.modle.FocusStars;
import com.qingye.wtsyou.modle.RecommendStars;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.view.home.HomeContentView;
import com.qingye.wtsyou.widget.CustomDialog;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeStarsTwoFragment extends BaseHttpRecyclerFragment<EntityHomeContent,HomeContentView,HomeContentAdapter> implements View.OnClickListener,CacheCallBack<EntityHomeContent>{

    private ImageView ivStars;
    private LinearLayout llfans,llconversation;
    private TextView tvName,tvFans,tvConversation;
    private Button btnRank;
    private Button btnFocus;

    private List<RecommendStars> recommendStarsList = new ArrayList<>();
    private RecommendStars recommendStar;
    private List<EntityHomeContent> campaignList = new ArrayList<>();

    private CustomDialog progressBar;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static HomeStarsTwoFragment createInstance() {

        return new HomeStarsTwoFragment();
    }

    public HomeStarsTwoFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_home_stars);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        getStars();

        initCache(this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //禁止滑动
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);
        rvBaseRecycler.setNestedScrollingEnabled(false);//解决卡顿
        rvBaseRecycler.setLayoutManager(linearLayoutManager);

        //srlBaseHttpRecycler.autoRefresh();
        srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(false);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(false);//尾部

        srlBaseHttpRecycler.autoRefresh();

        return view;
    }

    public void onResume() {
        //getStars();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        progressBarDismiss();
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
    public void initView() {
        super.initView();

        ivStars = findViewById(R.id.iv_stars_img);
        llfans = findView(R.id.ll_fans);
        llconversation = findView(R.id.ll_conversation);
        tvName = findViewById(R.id.tv_star_name);
        tvFans = findViewById(R.id.fans_num);
        tvConversation = findViewById(R.id.conversation_num);
        btnFocus = findViewById(R.id.btn_focus);
        btnRank = findViewById(R.id.btn_rank);
    }

    @Override
    public void setList(final List<EntityHomeContent> list) {

        setList(new AdapterCallBack<HomeContentAdapter>() {

            @Override
            public HomeContentAdapter createAdapter() {
                return new HomeContentAdapter(context);
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

        if (recommendStar != null) {
            tvName.setText(recommendStar.getStarName());
            tvFans.setText(recommendStar.getFunsCount());
            tvConversation.setText(recommendStar.getChatRoomCount());

            String url = recommendStar.getStarPhoto();
            Glide.with(context)
                    .load(url)
                    .into(ivStars);

            Boolean isFocus = recommendStar.getIsFollow();
            if (isFocus) {
                btnFocus.setVisibility(View.GONE);
                btnRank.setVisibility(View.VISIBLE);
            } else {
                btnFocus.setVisibility(View.VISIBLE);
                btnRank.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void getListAsync(int page) {

    }

    @Override
    public List<EntityHomeContent> parseArray(String json) {
        return null;
    }

    @Override
    public Class<EntityHomeContent> getCacheClass() {
        return EntityHomeContent.class;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(EntityHomeContent data) {
        return null;
    }

    @Override
    public int getCacheCount() {
        return 4;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivStars.setOnClickListener(this);

        llfans.setOnClickListener(this);
        llconversation.setOnClickListener(this);
        btnRank.setOnClickListener(this);
        btnFocus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_stars_img:

                break;
            case R.id.ll_fans:
                toActivity(SearchFansActivity.createIntent(context));
                break;
            case R.id.ll_conversation:
                toActivity(RecommendStarsConversationActivity.createIntent(context));
                break;
            case R.id.btn_focus:
                focusStars();
                break;
            case R.id.btn_rank:
                String uuid = recommendStarsList.get(1).getStarUuid();
                hit(uuid);
                break;
            default:
                break;
        }
    }

    //点击item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //toActivity(SaleDetailedActivity.createIntent(context,id));
    }

    public void getStars() {

        if (NetUtil.checkNetwork(context)) {

            HttpRequest.postHomeList(0, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityHomeList entityHomeList =  zuo.biao.library.util.JSON.parseObject(resultJson,EntityHomeList.class);
                        if(entityHomeList.isSuccess()){
                            //成功
                            //showShortToast(R.string.getSuccess);
                            recommendStarsList = entityHomeList.getContent().getHomeList();
                            if (recommendStarsList.size() > 0) {
                                recommendStar = recommendStarsList.get(1);
                            }

                            initData();

                            //获取该明星的活动列表
                            getHomeContent();

                        }else{//显示失败信息
                            if (entityHomeList.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityHomeList.getMessage());
                            }

                            progressBarDismiss();
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

    public void getHomeContent() {
        setProgressBar();
        progressBar.show();

        if (NetUtil.checkNetwork(context)) {
            String activityStates = null;
            String relevanceStar = recommendStar.getStarUuid();
            String cityName = null;

            HttpRequest.postCampaignList(0, activityStates, relevanceStar, cityName,new OnHttpResponseListener() {

                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityPageData entityPageData =  JSON.parseObject(resultJson,EntityPageData.class);
                        if(entityPageData.isSuccess()){
                            campaignList.clear();
                            //成功
                            //showShortToast(R.string.getSuccess);
                            List<EntityHomeContent> campaignLists = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageData.getContent().getData())
                                    ,new TypeToken<List<EntityHomeContent>>(){}.getType());

                            if (campaignLists.size() > 4) {
                                for (int i = 0;i < 4;i ++) {
                                    campaignList.add(campaignLists.get(i));
                                }
                            } else {
                                campaignList.addAll(campaignLists);
                            }

                            progressBarDismiss();

                            setList(campaignList);
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
        }
    }

    public void focusStars() {
        List<FocusStars> focusStarsRequestList = new ArrayList<>();
        if (recommendStarsList.size() > 0) {
                FocusStars focusStarsRequest = new FocusStars();
                focusStarsRequest.setStarUuid(recommendStarsList.get(1).getStarUuid());
                focusStarsRequest.setStarName(recommendStarsList.get(1).getStarName());
                focusStarsRequest.setStarPhoto(recommendStarsList.get(1).getStarPhoto());
                focusStarsRequestList.add(focusStarsRequest);
        }

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

                            progressBarDismiss();

                            btnFocus.setVisibility(View.GONE);
                            btnRank.setVisibility(View.VISIBLE);

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

    public void hit(String uuid) {

        setProgressBar();
        progressBar.show();

        //检查网络
        if (NetUtil.checkNetwork(getActivity())) {

            HttpRequest.postHit(0, uuid, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功//showShortToast(R.string.getSuccess);
                            showShortToast(R.string.hitSuccess);

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

}
