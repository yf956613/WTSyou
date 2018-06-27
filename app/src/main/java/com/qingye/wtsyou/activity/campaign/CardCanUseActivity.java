package com.qingye.wtsyou.activity.campaign;

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
import com.qingye.wtsyou.adapter.my.CardAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.Card;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.my.CardView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class CardCanUseActivity extends BaseHttpRecyclerActivity<Card,CardView,CardAdapter>
        implements View.OnClickListener, View.OnLongClickListener, CacheCallBack<Card>, OnBottomDragListener, OnHttpPageCallBack<EntityPageData,Card> {

    private ImageView ivBack;
    private TextView tvHead;

    private LinearLayout llHead;
    /*private ObservableScrollView scrollView;*/
    private View line;
    private int headHeight;

    private CustomDialog progressBar;

    private HttpPageModel<EntityPageData,Card> mEntityPageDataHttpModel;

    private List<Card> cardsList = new ArrayList<>();

    private double minAmount = 0;

   //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, double minAmount) {
        return new Intent(context,CardCanUseActivity.class).putExtra(Constant.MINAMOUNT, minAmount);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket, this);

        progressBar = new CustomDialog(getActivity(), R.style.CustomDialog);

        intent = getIntent();
        minAmount = intent.getDoubleExtra(Constant.MINAMOUNT, 0);

        //卡券列表
        mEntityPageDataHttpModel = new HttpPageModel<>(EntityPageData.class);
        cardsQuery();

        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

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
        tvHead.setText("可选择的优惠券");

        llHead = findView(R.id.ll_head);
        line = findView(R.id.line);
        /*scrollView = findView(R.id.scrollview);
        initListeners();*/
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
    public void setList(final List<Card> list) {
        setList(new AdapterCallBack<CardAdapter>() {

            @Override
            public CardAdapter createAdapter() {
                return new CardAdapter(context);
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
    public List<Card> parseArray(String json) {
        return null;
    }

    @Override
    public Class<Card> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(Card data) {
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
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.SELECTCARD, cardsList.get(position));//放进数据流中

        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
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

    public void cardsQuery() {
        setProgressBar();
        progressBar.show();

        //卡券列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.CARDQUERY,this);
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public List<Card> getList(EntityPageData data) {
        return GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(data.getContent().getData())
                ,new TypeToken<List<Card>>(){}.getType());
    }

    @Override
    public String getRequestJsonStr(int page, int pageSize) {
        String[] states = {"used","overdue"};
        String request = HttpRequest.postCardList(states, page, pageSize);

        return request;
    }

    @Override
    public void emptyPagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishRefresh();
    }

    @Override
    public void refreshSuccessPagingList(List<Card> list) {
        cardsList.clear();

        cardsList.addAll(list);
        srlBaseHttpRecycler.finishRefresh();
        srlBaseHttpRecycler.setLoadmoreFinished(false);

        setList(cardsList);
    }

    @Override
    public void noMorePagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();
    }

    @Override
    public void loadMoreSuccessPagingList(List<Card> list) {
        cardsList.addAll(list);
        srlBaseHttpRecycler.finishLoadmore();

        setList(cardsList);

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
        //卡券列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.CARDQUERY, this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //卡券列表
        mEntityPageDataHttpModel.loadMorePost(URL_BASE + URLConstant.CARDQUERY, this);
    }

}
