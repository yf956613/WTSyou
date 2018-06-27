package com.qingye.wtsyou.fragment.campaign;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.campaign.ConversationAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.campaign.ConversationView;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class OtherConversationFragment extends BaseHttpRecyclerFragment<ChatingRoom,ConversationView,ConversationAdapter>
        implements CacheCallBack<ChatingRoom>, OnHttpPageCallBack<EntityPageData,ChatingRoom> {

    private String[] selectStars = new String[3];

    private HttpPageModel<EntityPageData,ChatingRoom> mEntityPageDataHttpModel;

    private  List<ChatingRoom> chatingRoomList =  new ArrayList<>();

    private CustomDialog progressBar;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static OtherConversationFragment createInstance() {

        return new OtherConversationFragment();
    }

    public OtherConversationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_campaign_detailed_conversation);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //获取传来的数据
        Bundle bundle = getArguments();
        selectStars = bundle.getStringArray(Constant.SELECTED_STARS);

        //聊天室列表
        mEntityPageDataHttpModel = new HttpPageModel<>(EntityPageData.class);
        conversationQuery();

        initCache(this);

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

        return view;
    }

    @Override
    public void initView() {
        super.initView();
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
    public void setList(final List<ChatingRoom> list) {
        setList(new AdapterCallBack<ConversationAdapter>() {

            @Override
            public ConversationAdapter createAdapter() {
                return new ConversationAdapter(context);
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

    public void conversationQuery() {

        mEntityPageDataHttpModel.refreshPost( URL_BASE + URLConstant.CONVERSATIONQUERY,this);
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
        String topRecommend = null;
        String hotRecommend = null;
        String desc = "false";
        String excludeMyself = null;
        String sortKey = null;

        //聊天室列表
        String request = HttpRequest.postConversationQueryList(name, selectStars, ownerId, adminUserId, personId, status,
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
        chatingRoomList.clear();

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
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.CONVERSATIONQUERY, this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //聊天室列表
        mEntityPageDataHttpModel.loadMorePost(URL_BASE + URLConstant.CONVERSATIONQUERY, this);
    }
}
