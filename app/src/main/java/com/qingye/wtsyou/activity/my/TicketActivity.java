package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.my.TicketAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.Ticket;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.my.TicketView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class TicketActivity extends BaseHttpRecyclerActivity<Ticket,TicketView,TicketAdapter>
        implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener, OnHttpPageCallBack<EntityPageData, Ticket> {

    private ImageView ivBack;
    private TextView tvHead;

    private LinearLayout llHead;
    /*private ObservableScrollView scrollView;*/
    private View line;
    private int headHeight;

    private LinearLayout noMessage;

    private CustomDialog progressBar;

    private HttpPageModel<EntityPageData,Ticket> mTicketHttpModel;

    private List<Ticket> ticketList = new ArrayList<>();

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context,TicketActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //门票列表
        mTicketHttpModel = new HttpPageModel<>(EntityPageData.class);
        ticketQuery();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        /*//禁止滑动
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);
        linearLayoutManager.setScrollEnabled(false);
        rvBaseRecycler.setNestedScrollingEnabled(false);//解决卡顿
        rvBaseRecycler.setLayoutManager(linearLayoutManager);*/

        srlBaseHttpRecycler.autoRefresh();
        /*srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(false);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(false);//尾部*/
    }

    @Override
    public void initView() {
        super.initView();

        ivBack = findView(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("我的门票");

        llHead = findView(R.id.ll_head);
        line = findView(R.id.line);
        /*scrollView = findView(R.id.scrollview);*/
        /*initListeners();*/

        noMessage = findView(R.id.noMessage);
    }

    /*private void initListeners() {
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = llHead.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llHead.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                headHeight = llHead.getHeight();
                scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
                    @Override
                    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                        // TODO Auto-generated method stub
                        if (y <= 0) {
                            line.setVisibility(View.GONE);
                        } else if (y > 0 && y <= headHeight) {
                            line.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });


    }*/

    @Override
    public void setList(final List<Ticket> list) {

        setList(new AdapterCallBack<TicketAdapter>() {

            @Override
            public TicketAdapter createAdapter() {
                return new TicketAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
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
        super.initData();
    }

    @Override
    public void getListAsync(int page) {

    }

    @Override
    public List<Ticket> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left:
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toActivity(TicketDetailedActivity.createIntent(context, ticketList.get(position)));
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

    public void ticketQuery() {
        /*setProgressBar();
        progressBar.show();*/


        //门票列表
        mTicketHttpModel.refreshPost(URL_BASE + URLConstant.TICKETQUERY,this);
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public List<Ticket> getList(EntityPageData data) {
        return GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(data.getContent().getData())
                ,new TypeToken<List<Ticket>>(){}.getType());
    }

    @Override
    public String getRequestJsonStr(int page, int pageSize) {
        String paymentState = "payed";
        String parts =  "activity";
        Boolean showFail = true;
        String activityProperty = null;

        String request = HttpRequest.postTicketList( paymentState, parts, showFail, activityProperty, page, pageSize);
        return request;
    }

    @Override
    public void emptyPagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishRefresh();

        if (ticketList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refreshSuccessPagingList(List<Ticket> list) {
        ticketList.clear();

        ticketList.addAll(list);
        srlBaseHttpRecycler.finishRefresh();
        srlBaseHttpRecycler.setLoadmoreFinished(false);

        setList(ticketList);

        if (ticketList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void noMorePagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();

        if (ticketList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadMoreSuccessPagingList(List<Ticket> list) {
        ticketList.addAll(list);
        srlBaseHttpRecycler.finishLoadmore();

        setList(ticketList);

        if (ticketList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refreshErrorPagingList() {
        showShortToast(R.string.noReturn);

        if (ticketList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadMoreErrorPagingList() {
        showShortToast(R.string.noReturn);

        if (ticketList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        //门票列表
        mTicketHttpModel.refreshPost(URL_BASE + URLConstant.TICKETQUERY, this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //门票列表
        mTicketHttpModel.loadMorePost(URL_BASE + URLConstant.TICKETQUERY, this);
    }
}
