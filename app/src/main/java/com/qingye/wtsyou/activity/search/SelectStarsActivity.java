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
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.MainTabActivity;
import com.qingye.wtsyou.adapter.home.SelectStarsAdapter;
import zuo.biao.library.model.EntityBase;
import com.qingye.wtsyou.fragment.home.SelectedStarsFragment;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.EntityStars;
import com.qingye.wtsyou.model.EntityStarsItem;
import com.qingye.wtsyou.model.FocusStars;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.view.home.SelectStarsView;
import zuo.biao.library.widget.CustomDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.StringUtil;

public class SelectStarsActivity extends BaseHttpRecyclerActivity <EntityStarsItem,SelectStarsView,SelectStarsAdapter> implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivSearch;
    private TextView tvHead;
    private TextView tvConfirm;

    //全部明星
    private List<EntityStarsItem> starsItem = new ArrayList<>();
    //选择的明星
    private Map<String,EntityStars> MapselectedStars = new HashMap<>();
    private List<EntityStars> selectedStars = new ArrayList<>();
    //取消关注的明星
    private Map<String,EntityStars> MapcancelStars = new HashMap<>();
    private List<EntityStars> cancelStars = new ArrayList<>();

    private LinearLayout listview;
    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    private int WHICH;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, int which) {
        return new Intent(context,SelectStarsActivity.class).putExtra(Constant.WHICH, which);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stars,this);

        intent = getIntent();
        WHICH = intent.getIntExtra(Constant.WHICH, 0);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        allStars();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(false);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(false);//尾部

        //实例化一个GridLayoutManager，列数为3
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        rvBaseRecycler.setLayoutManager(layoutManager);

        srlBaseHttpRecycler.autoRefresh();
    }

    @Override
    public void initView() {
        super.initView();
        listview = findViewById(R.id.list_all_stars);
        swipeRefresh = findViewById(R.id.swipe_refresh_widget);

        ivBack = findViewById(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.backwirtih);
        ivSearch = findViewById(R.id.iv_right);
        ivSearch.setImageResource(R.mipmap.search);
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("全部明星");
        tvConfirm = findViewById(R.id.tv_confirm);
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
                        allStars();
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
                return listview.getScrollY() > 0;
            }
        });
    }

    @Override
    public void setList(final List<EntityStarsItem> list) {

        setList(new AdapterCallBack<SelectStarsAdapter>() {

            @Override
            public SelectStarsAdapter createAdapter() {
                return new SelectStarsAdapter(context);
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
    public List<EntityStarsItem> parseArray(String json) {
        return starsItem;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                toActivity(SearchStarsActivity.createIntent(context));
                break;
            case R.id.tv_confirm:
                focusStars();
                cancelStars();
                break;
            default:
                break;
        }

    }

    public void allStars() {
        if (NetUtil.checkNetwork(this)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.postAllStars(0, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityPageData entityPageData =  JSON.parseObject(resultJson,EntityPageData.class);
                        if(entityPageData.isSuccess()){
                            //成功
                            //showShortToast(R.string.getSuccess);
                            List<EntityStars> stars = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageData.getContent().getData())
                                    ,new TypeToken<List<EntityStars>>(){}.getType());
                            starsItem.clear();
                            selectedStars.clear();
                            for (EntityStars entityStars : stars) {
                                EntityStarsItem entityStarsItem = new EntityStarsItem();
                                entityStarsItem.setEntityStars(entityStars);
                                entityStarsItem.setSelector(false);
                                starsItem.add(entityStarsItem);

                                if (entityStarsItem.getEntityStars().getIsFollow()) {
                                    entityStarsItem.setSelector(true);

                                    MapselectedStars.put(entityStarsItem.getEntityStars().getUuid(),entityStarsItem.getEntityStars());
                                    selectedStars.add(entityStars);
                                }
                            }
                            setList(starsItem);
                            selectedStars(selectedStars);

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
        }
    }

    //传递选择的明星
    public void selectedStars(List<EntityStars> selectStars) {
        SelectedStarsFragment selectedStarsFragment = new SelectedStarsFragment();
        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.SELECTED_STARS, (Serializable) selectStars);
        selectedStarsFragment.setArguments(bundle);
        //把碎片添加到碎片中
        transaction.replace(R.id.list_selected_stars,selectedStarsFragment);
        transaction.commit();
    }

    //更新选择的明星
    public void getSelectedStars() {

        selectedStars.clear();
        for (Map.Entry<String, EntityStars> entry : MapselectedStars.entrySet()) {
            selectedStars.add(entry.getValue());
        }
    }

    //取消关注的明星
    public void getCancelStars() {

        cancelStars.clear();
        for (Map.Entry<String, EntityStars> entry : MapcancelStars.entrySet()) {
            cancelStars.add(entry.getValue());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //此处可选择多个明星
        if (starsItem.get(position).getSelector()) {
            starsItem.get(position).setSelector(false);
            starsItem.get(position).getEntityStars().setIsFollow(false);
        } else {
            starsItem.get(position).setSelector(true);
            starsItem.get(position).getEntityStars().setIsFollow(true);
        }

        //更新界面
        setList(starsItem);

        //选择的明星
        if (starsItem.get(position).getSelector()) {
            MapselectedStars.put(starsItem.get(position).getEntityStars().getUuid(),starsItem.get(position).getEntityStars());
            MapcancelStars.remove(starsItem.get(position).getEntityStars().getUuid());
        } else {
            MapselectedStars.remove(starsItem.get(position).getEntityStars().getUuid());
            MapcancelStars.put(starsItem.get(position).getEntityStars().getUuid(),starsItem.get(position).getEntityStars());
        }

        //判断选择数量
        if (MapselectedStars.size() < 1) {
            showShortToast(R.string.leastOne);
            MapselectedStars.put(starsItem.get(position).getEntityStars().getUuid(),starsItem.get(position).getEntityStars());
            MapselectedStars.remove(starsItem.get(position).getEntityStars().getUuid());
        }

        getSelectedStars();
        getCancelStars();
        selectedStars(selectedStars);

    }

    public void focusStars() {
        List<FocusStars> focusStarsRequestList = new ArrayList<>();
        for (int i = 0;i < selectedStars.size(); i ++) {
            FocusStars focusStarsRequest = new FocusStars();
            focusStarsRequest.setStarUuid(selectedStars.get(i).getUuid());
            focusStarsRequest.setStarName(selectedStars.get(i).getName());
            focusStarsRequest.setStarPhoto(selectedStars.get(i).getPhoto());
            focusStarsRequestList.add(focusStarsRequest);
        }

        if (NetUtil.checkNetwork(this)) {
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

                            if (WHICH == 1) {
                                finish();
                            }
                            else if (WHICH == 2) {
                                toActivity(MainTabActivity.createIntent(context));
                                finish();
                            }

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

    public void cancelStars() {
        List<FocusStars> cancelStarsRequestList = new ArrayList<>();
        for (int i = 0;i < cancelStars.size(); i ++) {
            FocusStars cancelStarsRequest = new FocusStars();
            cancelStarsRequest.setStarUuid(cancelStars.get(i).getUuid());
            cancelStarsRequest.setStarName(cancelStars.get(i).getName());
            cancelStarsRequest.setStarPhoto(cancelStars.get(i).getPhoto());
            cancelStarsRequestList.add(cancelStarsRequest);
        }

        if (NetUtil.checkNetwork(this)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.postCancelStars(0, cancelStarsRequestList, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  zuo.biao.library.util.JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功
                            showShortToast(R.string.cancelFocusSuccess);

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
}
