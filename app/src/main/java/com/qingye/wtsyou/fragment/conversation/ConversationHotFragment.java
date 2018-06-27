package com.qingye.wtsyou.fragment.conversation;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.conversation.ConversationHotAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.conversation.ConversationHotView;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationHotFragment extends BaseHttpRecyclerFragment<ChatingRoom,ConversationHotView,ConversationHotAdapter>
        implements CacheCallBack<ChatingRoom>, OnHttpPageCallBack<EntityPageData,ChatingRoom> {

    private CustomDialog progressBar;

    private HttpModel<EntityPageData> mHotChatingRoomHttpModel;
    private HttpPageModel<EntityPageData,ChatingRoom> mChatingRoomHttpModel;

    private  List<ChatingRoom> chatingRoomList =  new ArrayList<>();

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static ConversationHotFragment createInstance() {

        return new ConversationHotFragment();
    }

    public ConversationHotFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_converesation_hot);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //热门聊天室
        mHotChatingRoomHttpModel = new HttpModel<>(EntityPageData.class);
        //聊天室列表
        mChatingRoomHttpModel = new HttpPageModel<>(EntityPageData.class);
        hotConversationQuery();

        initCache(this);

        /*//获取传来的数据
        Bundle bundle = getArguments();
        chatingRoomList = (List<ChatingRoom>) bundle.getSerializable(Constant.HOTRECOMMEND);*/

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //禁止滑动
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);
        linearLayoutManager.setScrollEnabled(true);
        rvBaseRecycler.setNestedScrollingEnabled(true);//解决卡顿
        rvBaseRecycler.setLayoutManager(linearLayoutManager);

        srlBaseHttpRecycler.autoRefresh();
        srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(true);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(true);//尾部

        //实例化一个GridLayoutManager，列数为2
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rvBaseRecycler.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void setList(final List<ChatingRoom> list) {

        setList(new AdapterCallBack<ConversationHotAdapter>() {

            @Override
            public ConversationHotAdapter createAdapter() {
                return new ConversationHotAdapter(context);
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
    public void initData() {//必须调用
        super.initData();

    }

    @Override
    public List<ChatingRoom> parseArray(String json) {
        return null;
    }

    @Override
    public Class<ChatingRoom> getCacheClass() {
        return null;
    }

    @Override
    public String getCacheGroup() {
        return null;
    }

    @Override
    public String getCacheId(ChatingRoom data) {
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

    }

    public void hotConversationQuery() {

        String name = null;
        String ownerId = null;
        String adminUserId = null;
        String personId = null;
        String status = null;
        String topRecommend = null;
        String hotRecommend = "true";
        String desc = "false";
        String excludeMyself = null;
        String sortKey = null;

        String request = HttpRequest.postConversationQueryList(name, null, ownerId, adminUserId, personId, status, topRecommend, hotRecommend,
                desc, excludeMyself, sortKey);
        mHotChatingRoomHttpModel.post(request, URL_BASE + URLConstant.CONVERSATIONQUERY, 1,this);
    }

    public void conversationQuery() {

        mChatingRoomHttpModel.refreshPost( URL_BASE + URLConstant.CONVERSATIONQUERY,this);
    }

    @Override
    public void Success(String url, int RequestCode, EntityBase entityBase) {
        super.Success(url, RequestCode, entityBase);
        switch (RequestCode) {
            case 1:
                EntityPageData.Content entityPageDatafirst = mHotChatingRoomHttpModel.getData().getContent();
                chatingRoomList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageDatafirst.getData())
                        ,new TypeToken<List<ChatingRoom>>(){}.getType());
                conversationQuery();
                break;
        }
    }


    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public List<ChatingRoom> getList(EntityPageData data) {
        return GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(data.getContent().getData())
                ,new TypeToken<List<ChatingRoom>>(){}.getType());
    }

    @Override
    public String getRequestJsonStr(int page, int pageSize) {

        String name = null;
        String ownerId = null;
        String adminUserId = null;
        String personId = null;
        String status = null;
        String topRecommend = "false";
        String hotRecommend = "false";
        String desc = "false";
        String excludeMyself = null;
        String sortKey = null;

        //聊天室列表
        String request = HttpRequest.postConversationQueryList(name, null, ownerId, adminUserId, personId, status,
                topRecommend,hotRecommend, page, pageSize, desc, excludeMyself, sortKey);

        return request;
    }

    @Override
    public void emptyPagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishRefresh();
    }

    @Override
    public void refreshSuccessPagingList(List<ChatingRoom> list) {
        //chatingRoomList.clear();

        chatingRoomList.addAll(list);
        srlBaseHttpRecycler.finishRefresh();
        srlBaseHttpRecycler.setLoadmoreFinished(false);

        setList(chatingRoomList);
    }

    @Override
    public void noMorePagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();
    }

    @Override
    public void loadMoreSuccessPagingList(List<ChatingRoom> list) {
        chatingRoomList.addAll(list);
        srlBaseHttpRecycler.finishLoadmore();

        setList(chatingRoomList);
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
        //聊天室列表
        mChatingRoomHttpModel.refreshPost(URL_BASE + URLConstant.CONVERSATIONQUERY, this);
        hotConversationQuery();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //聊天室列表
        mChatingRoomHttpModel.loadMorePost(URL_BASE + URLConstant.CONVERSATIONQUERY, this);
    }

}
