package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.home.RuleActivity;
import com.qingye.wtsyou.adapter.my.DiamondDetailedAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.DiamondDetailed;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.EntityPersonalMessage;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.my.DiamondDetailedView;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class DiamondActivity extends BaseHttpRecyclerActivity<DiamondDetailed,DiamondDetailedView,DiamondDetailedAdapter>
        implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener, OnHttpPageCallBack<EntityPageData, DiamondDetailed> {

    private ImageView ivBack,ivRule;
    private TextView tvHead;
    private TextView tvValue;
    private Button btnRecharge;

    private ScrollView scrollView;
    private SwipeRefreshLayout swipeRefresh;
    private RelativeLayout linearLayout;
    private CustomDialog progressBar;

    FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);

    private HttpPageModel<EntityPageData, DiamondDetailed> mEntityPageDataHttpModel;

    private int diamondCount;
    List<DiamondDetailed> diamondDetailedList= new ArrayList<>();

    private EntityPersonalMessage entityPersonalMessage;

    private HttpModel<EntityPersonalMessage> mEntityPersonalMessageHttpModel;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, DiamondActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diamond,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //获取详情
        mEntityPersonalMessageHttpModel = new HttpModel<>(EntityPersonalMessage.class);
        getPersonalMessage();

        //钻石列表
        mEntityPageDataHttpModel = new HttpPageModel<>(EntityPageData.class);
        getDiamondDetailed();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //禁止滑动
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);
        linearLayoutManager.setScrollEnabled(false);
        rvBaseRecycler.setNestedScrollingEnabled(false);//解决卡顿
        rvBaseRecycler.setLayoutManager(linearLayoutManager);

        srlBaseHttpRecycler.autoRefresh();
        /*srlBaseHttpRecycler.setEnableRefresh(true);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(true);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(true);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(true);//尾部*/

    }

    @Override
    public void initView() {
        super.initView();
        swipeRefresh = findView(R.id.swipe_refresh_widget);
        linearLayout = findView(R.id.linearlayout);
        scrollView = findView(R.id.scrollview);
        scrollView.setOnTouchListener(new TouchListenerImpl());

        ivBack = findView(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_l);
        ivRule = findView(R.id.iv_right);
        ivRule.setImageResource(R.mipmap.rule_w);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("我的钻石");
        tvHead.setTextColor(getResources().getColor(R.color.white));
        btnRecharge = findView(R.id.btn_recharge);

        tvValue = findView(R.id.tv_value);
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

                        getPersonalMessage();
                        getDiamondDetailed();
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

    private class TouchListenerImpl implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    break;
                case MotionEvent.ACTION_MOVE:
                    int scrollY=view.getScrollY();
                    int height=view.getHeight();
                    int scrollViewMeasuredHeight = scrollView.getChildAt(0).getMeasuredHeight();
                    if(scrollY == 0){

                    }
                    else if((scrollY+height) == scrollViewMeasuredHeight){
                        setScrollEnable(true);
                    } else {
                        setScrollEnable(false);
                    }
                    break;

                default:
                    break;
            }
            return false;
        }

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
    public void setList(final List<DiamondDetailed> list) {

        setList(new AdapterCallBack<DiamondDetailedAdapter>() {

            @Override
            public DiamondDetailedAdapter createAdapter() {
                return new DiamondDetailedAdapter(context);
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

        tvValue.setText(Integer.toString(diamondCount));
    }

    @Override
    public void getListAsync(int page) {

    }

    @Override
    public List<DiamondDetailed> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
        ivRule.setOnClickListener(this);
        btnRecharge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                toActivity(RuleActivity.createIntent(context, "diamond", "钻石规则"));
                break;
            case R.id.btn_recharge:
                toActivity(RechargeActivity.createIntent(context));
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

    public void getPersonalMessage() {
        setProgressBar();
        progressBar.show();
        mEntityPersonalMessageHttpModel.get(URL_BASE + URLConstant.GETPERSONALMESSAGE,1,this);
    }

    @Override
    public void Success(String url, int RequestCode, EntityBase entityBase) {
        super.Success(url, RequestCode, entityBase);
        switch (RequestCode) {
            case 1:
                entityPersonalMessage = mEntityPersonalMessageHttpModel.getData();
                diamondCount = entityPersonalMessage.getContent().getDiamondCount();
                initData();
                break;
        }
    }

    public void getDiamondDetailed() {
        setProgressBar();
        progressBar.show();

        //明星列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.DIAMONDQUERY, this);
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public List<DiamondDetailed> getList(EntityPageData data) {
        return GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(data.getContent().getData())
                ,new TypeToken<List<DiamondDetailed>>(){}.getType());
    }

    @Override
    public String getRequestJsonStr(int page, int pageSize) {
        String keywords = null;
        String request = HttpRequest.postKeywordsPageData(keywords, page, pageSize);
        return request;
    }

    @Override
    public void emptyPagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishRefresh();
    }

    @Override
    public void refreshSuccessPagingList(List<DiamondDetailed> list) {
        diamondDetailedList.clear();

        diamondDetailedList.addAll(list);
        setList(diamondDetailedList);
        srlBaseHttpRecycler.finishRefresh();
        srlBaseHttpRecycler.setLoadmoreFinished(false);

    }

    @Override
    public void noMorePagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();
    }

    @Override
    public void loadMoreSuccessPagingList(List<DiamondDetailed> list) {
        diamondDetailedList.addAll(list);
        srlBaseHttpRecycler.finishLoadmore();

        setList(diamondDetailedList);

    }

    @Override
    public void refreshErrorPagingList() {
        showShortToast(R.string.noReturn);
    }

    @Override
    public void loadMoreErrorPagingList() {
        showShortToast(R.string.noReturn);
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);

        getPersonalMessage();
        //钻石列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.DIAMONDQUERY, this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //钻石列表
        mEntityPageDataHttpModel.loadMorePost(URL_BASE + URLConstant.DIAMONDQUERY, this);
    }

    public void setScrollEnable(Boolean enable) {
        linearLayoutManager.setScrollEnabled(enable);
        rvBaseRecycler.setNestedScrollingEnabled(enable);//解决卡顿
        rvBaseRecycler.setLayoutManager(linearLayoutManager);
    }
}
