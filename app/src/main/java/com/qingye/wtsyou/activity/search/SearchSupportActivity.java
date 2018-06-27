package com.qingye.wtsyou.activity.search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.campaign.SupportDetailedActivity;
import com.qingye.wtsyou.adapter.campaign.ActivityNewSupportAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.Supports;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.campaign.ActivityNewSupportView;
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

public class SearchSupportActivity extends BaseHttpRecyclerActivity<Supports,ActivityNewSupportView,ActivityNewSupportAdapter> implements CacheCallBack<Supports>,
        View.OnClickListener, View.OnLongClickListener, OnBottomDragListener, OnHttpPageCallBack<EntityPageData, Supports> {

    private EditText edtContent;
    private RelativeLayout rlCancel;
    private LinearLayout llSupport;

    private HttpPageModel<EntityPageData,Supports> mEntityPageDataHttpModel;

    private CustomDialog progressBar;

    private List<Supports> supportList = new ArrayList<>();

    private String keywords = null;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, SearchSupportActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_support,this);

        //应援列表
        mEntityPageDataHttpModel = new HttpPageModel<>(EntityPageData.class);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>


        //禁止滑动
        /*FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);
        linearLayoutManager.setScrollEnabled(false);
        rvBaseRecycler.setNestedScrollingEnabled(false);//解决卡顿
        rvBaseRecycler.setLayoutManager(linearLayoutManager);*/

        /*srlBaseHttpRecycler.setEnableRefresh(true);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(true);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(true);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(true);//尾部*/
    }

    @Override
    public void initView() {
        super.initView();

        edtContent = findView(R.id.edit_search_content);
        edtContent.setHint("搜索应援");
        rlCancel = findView(R.id.rl_cancel);

        edtContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_SEND
                        || keyCode == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    // do some your things
                    srlBaseHttpRecycler.autoRefresh();
                    if (edtContent.getText().toString().isEmpty()) {
                        keywords = null;
                        supportQuery();
                    } else {
                        keywords = edtContent.getText().toString().trim();
                        supportQuery();
                    }
                }
                return false;
            }
        });

        llSupport = findView(R.id.llSupport);
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
    public void setList(final List<Supports> list) {
        setList(new AdapterCallBack<ActivityNewSupportAdapter>() {

            @Override
            public ActivityNewSupportAdapter createAdapter() {
                return new ActivityNewSupportAdapter(context);
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
    public List<Supports> parseArray(String json) {
        return null;
    }

    @Override
    public Class<Supports> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(Supports data) {
        return null;
    }

    @Override
    public int getCacheCount() {
        return 0;
    }

    @Override
    public void initEvent() {
        super.initEvent();

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

    //点击item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        toActivity(SupportDetailedActivity.createIntent(context, supportList.get(position).getActivityId()));
    }

    public void supportQuery() {

        setProgressBar();
        progressBar.show();

        //应援列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.SUPPORTQUERY, this);
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public List<Supports> getList(EntityPageData data) {
        return GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(data.getContent().getData())
                ,new TypeToken<List<Supports>>(){}.getType());
    }

    @Override
    public String getRequestJsonStr(int page, int pageSize) {
        String userId = null;
        String activityId = null;
        String activityProperty = "support";

        String request = HttpRequest.postSupportQuery(userId, activityId, activityProperty, keywords, page, pageSize);
        return request;
    }

    @Override
    public void emptyPagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishRefresh();
    }

    @Override
    public void refreshSuccessPagingList(List<Supports> list) {
        supportList.clear();

        supportList.addAll(list);
        srlBaseHttpRecycler.finishRefresh();
        srlBaseHttpRecycler.setLoadmoreFinished(false);

        if (supportList.size() > 0) {
            llSupport.setVisibility(View.VISIBLE);
        } else {
            llSupport.setVisibility(View.GONE);
        }

        setList(supportList);
    }

    @Override
    public void noMorePagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();
    }

    @Override
    public void loadMoreSuccessPagingList(List<Supports> list) {
        supportList.addAll(list);
        srlBaseHttpRecycler.finishLoadmore();

        if (supportList.size() > 0) {
            llSupport.setVisibility(View.VISIBLE);
        } else {
            llSupport.setVisibility(View.GONE);
        }
        setList(supportList);

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
        //应援列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.SUPPORTQUERY, this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //应援列表
        mEntityPageDataHttpModel.loadMorePost(URL_BASE + URLConstant.SUPPORTQUERY, this);
    }

}
