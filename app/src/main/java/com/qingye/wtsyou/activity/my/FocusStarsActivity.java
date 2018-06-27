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

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.search.SelectStarsActivity;
import com.qingye.wtsyou.adapter.home.FocusStarsAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.FocusStars;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.home.FocusStarsView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class FocusStarsActivity extends BaseHttpRecyclerActivity<FocusStars,FocusStarsView,FocusStarsAdapter>
        implements View.OnClickListener, OnBottomDragListener, FocusStarsView.OnItemChildClickListener, OnHttpPageCallBack<EntityPageData, FocusStars> {

    private ImageView ivLeft,ivAddStars;
    private TextView tvHead;

    private LinearLayout noMessage;

    private FocusStarsAdapter focusStarsAdapter;

    private HttpPageModel<EntityPageData, FocusStars> mEntityPageDataHttpModel;

    private List<FocusStars> focusStarsList = new ArrayList<>();

    private LinearLayout linearLayout;
    private CustomDialog progressBar;
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
            return new Intent(context, FocusStarsActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
            return this; //必须return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_stars,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //关注的明星列表
        mEntityPageDataHttpModel = new HttpPageModel<>(EntityPageData.class);
        focuedStars();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        srlBaseHttpRecycler.autoRefresh();
        /*srlBaseHttpRecycler.setEnableRefresh(true);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(true);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(true);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(true);//尾部*/
    }

    @Override
    public void initView() {
        super.initView();
        linearLayout = findView(R.id.linearlayout);

        ivLeft = findView(R.id.iv_left);
        ivLeft.setImageResource(R.mipmap.back_a);
        ivAddStars = findView(R.id.iv_right);
        ivAddStars.setImageResource(R.mipmap.add);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("关注列表");

        noMessage = findView(R.id.noMessage);
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
    public void setList(final List<FocusStars> list) {

        setList(new AdapterCallBack<FocusStarsAdapter>() {

            @Override
            public FocusStarsAdapter createAdapter() {

                focusStarsAdapter = new FocusStarsAdapter(context);
                focusStarsAdapter.setOnItemChildClickListener(FocusStarsActivity.this);

                return focusStarsAdapter;
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }
        });
    }


    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Override
    public void initData() {//必须调用
        super.initData();

    }

    @Override
    public void getListAsync(int page) {

    }

    @Override
    public List<FocusStars> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {//必须调用
        super.initEvent();
        ivLeft.setOnClickListener(this);
        ivAddStars.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                toActivity(SelectStarsActivity.createIntent(context,3));
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

    public void focuedStars() {
        /*setProgressBar();
        progressBar.show();*/

        //我关注的明星列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.FOCUEDSTARS, this);
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public List<FocusStars> getList(EntityPageData data) {
        return GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(data.getContent().getData())
                ,new TypeToken<List<FocusStars>>(){}.getType());
    }

    @Override
    public String getRequestJsonStr(int page, int pageSize) {
        String request = HttpRequest.postPageData( page, pageSize);
        return request;
    }

    @Override
    public void emptyPagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishRefresh();

        if (focusStarsList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refreshSuccessPagingList(List<FocusStars> list) {
        focusStarsList.clear();
        focusStarsList.addAll(list);
        setList(focusStarsList);
        srlBaseHttpRecycler.finishRefresh();
        srlBaseHttpRecycler.setLoadmoreFinished(false);

        if (focusStarsList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void noMorePagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();

        if (focusStarsList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadMoreSuccessPagingList(List<FocusStars> list) {
        focusStarsList.addAll(list);
        setList(focusStarsList);
        srlBaseHttpRecycler.finishLoadmore();

        if (focusStarsList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void refreshErrorPagingList() {
        showShortToast(R.string.noReturn);

        if (focusStarsList.size() > 0) {
            noMessage.setVisibility(View.GONE);
        } else {
            noMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadMoreErrorPagingList() {
        showShortToast(R.string.noReturn);

        if (focusStarsList.size() > 0) {
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
        //已关注的明星列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.FOCUEDSTARS, this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //已关注的明星列表
        mEntityPageDataHttpModel.loadMorePost(URL_BASE + URLConstant.FOCUEDSTARS, this);
    }

    @Override
    public void onHitClick(View view, int position) {
        String uuid = focusStarsList.get(position).getUuid();
        hit(uuid);
    }
}
