package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.campaign.SelectConversationAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.model.ChatingRoomItem;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.campaign.SelectConversationView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class CreatorAssociateConversationActivity extends BaseHttpRecyclerActivity<ChatingRoomItem,SelectConversationView,SelectConversationAdapter>
        implements View.OnClickListener, OnBottomDragListener, OnHttpPageCallBack<EntityPageData, ChatingRoom> {

    private ImageView ivLeft;
    private TextView tvHead,tvRight;

    private HttpPageModel<EntityPageData, ChatingRoom> mEntityPageDataHttpModel;
    private HttpModel<EntityBase> mVoteHttpModel;
    private HttpModel<EntityBase> mSupportHttpModel;

    private CustomDialog progressBar;

    private String[] selectedStars = new String[3];

    private List<ChatingRoom> chatingRoomList = new ArrayList<>();
    private List<ChatingRoomItem> chatingRoomItemList = new ArrayList<>();
    //选择的聊天室
    private Map<String,ChatingRoom> MapSelectedConversation = new HashMap<>();
    private List<ChatingRoom> selectedConversation = new ArrayList<>();

    private String fromWhere;
    private String activityId;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, String[] selectStars, String fromWhere, String activityId) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(Constant.SELECTED_STARS, selectStars);
        return new Intent(context, CreatorAssociateConversationActivity.class)
                .putExtras(bundle)
                .putExtra(Constant.WHICH, fromWhere)
                .putExtra(Constant.ACTIVITYID, activityId);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_stars_conversation,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        intent = getIntent();
        selectedStars = intent.getExtras().getStringArray(Constant.SELECTED_STARS);
        fromWhere = intent.getStringExtra(Constant.WHICH);
        activityId = intent.getStringExtra(Constant.ACTIVITYID);

        //投票处进来
        mVoteHttpModel = new HttpModel<>(EntityBase.class);
        //应援处进来
        mSupportHttpModel = new HttpModel<>(EntityBase.class);
        //根据明星id查询聊天室
        mEntityPageDataHttpModel = new HttpPageModel<>(EntityPageData.class);
        conversationQuery();

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

        //实例化一个GridLayoutManager，列数为2
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rvBaseRecycler.setLayoutManager(layoutManager);

    }

    @Override
    public void initView() {
        super.initView();
        ivLeft = findView(R.id.iv_left);
        ivLeft.setImageResource(R.mipmap.back_a);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("选择聊天室");
        tvRight = findView(R.id.tv_add_temp);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTextColor(getResources().getColor(R.color.black_text1));
        tvRight.setText("确认");
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
    public void setList(final List<ChatingRoomItem> list) {

        setList(new AdapterCallBack<SelectConversationAdapter>() {

            @Override
            public SelectConversationAdapter createAdapter() {
                return new SelectConversationAdapter(context);
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
    public List<ChatingRoomItem> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {//必须调用
        super.initEvent();
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_add_temp:
                if (MapSelectedConversation.size() > 0 && MapSelectedConversation.size() < 4) {
                    setProgressBar();
                    progressBar.show();

                    String[] chatingRooms = new String[selectedConversation.size()];
                    if (selectedConversation.size() > 0) {

                        for (int i = 0;i < selectedConversation.size();i ++) {
                            chatingRooms[i] = selectedConversation.get(i).getId();
                        }
                    } else {
                        chatingRooms = null;
                    }
                    String request = HttpRequest.postAssociateConversation(activityId, chatingRooms);
                    if (fromWhere.equals("vote")) {
                        mVoteHttpModel.post(request,URL_BASE + URLConstant.VOTEASSOCIATECONVERSATION,1,this);
                    }
                    else if (fromWhere.equals("support")) {
                        mSupportHttpModel.post(request,URL_BASE + URLConstant.SUPPORTASSOCIATECONVERSATION,2,this);
                    }
                } else {
                    showShortToast(R.string.leastOneConversation);
                }
                break;
            default:
                break;
        }
    }

    //更新选择的聊天室
    public void getSelectedConversation() {
        //selectedStars更新后的map
        selectedConversation.clear();
        for (Map.Entry<String, ChatingRoom> entry : MapSelectedConversation.entrySet()) {
            selectedConversation.add(entry.getValue());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //此处可选择多个明星
        if (chatingRoomItemList.get(position).getSelector()) {
            chatingRoomItemList.get(position).setSelector(false);
        } else {
            chatingRoomItemList.get(position).setSelector(true);
        }

        //更新界面
        setList(chatingRoomItemList);

        //选择的明星
        if (chatingRoomItemList.get(position).getSelector()) {
            MapSelectedConversation.put(chatingRoomItemList.get(position).getChatingRoom().getId(),
                    chatingRoomItemList.get(position).getChatingRoom());
        } else {//移除的时候看看有没有已经在里面，因为上一个界面会传值过来
            MapSelectedConversation.remove(chatingRoomItemList.get(position).getChatingRoom().getId());
        }

        if (MapSelectedConversation.size() < 1) {
            showShortToast(R.string.leastOneConversation);
            chatingRoomItemList.get(position).setSelector(true);
            MapSelectedConversation.put(chatingRoomItemList.get(position).getChatingRoom().getId(),
                    chatingRoomItemList.get(position).getChatingRoom());
        }
        else if (MapSelectedConversation.size() > 3){
            showShortToast(R.string.mostThreeConversation);
            chatingRoomItemList.get(position).setSelector(false);
            MapSelectedConversation.remove(chatingRoomItemList.get(position).getChatingRoom().getId());
        }

        putSelectedMap(chatingRoomItemList);
        getSelectedConversation();
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

    private void putSelectedMap(List<ChatingRoomItem> list) {
        for (ChatingRoomItem chatingRoomItem : list) {

            if (chatingRoomItem.getSelector()) {
                MapSelectedConversation.put(chatingRoomItem.getChatingRoom().getId(), chatingRoomItem.getChatingRoom());
                selectedConversation.add(chatingRoomItem.getChatingRoom());
            }
        }
    }

    public void conversationQuery() {

        setProgressBar();
        progressBar.show();

        //聊天室列表
        mEntityPageDataHttpModel.refreshPost(URL_BASE + URLConstant.CONVERSATIONQUERY, this);

    }

    public List<ChatingRoomItem> setDefault(List<ChatingRoom> chatingRooms) {
        List<ChatingRoomItem> chatingRoomItems = new ArrayList<>();
        for (int i = 0;i < chatingRooms.size();i ++) {
            ChatingRoomItem chatingRoomItem = new ChatingRoomItem();
            chatingRoomItem.setChatingRoom(chatingRooms.get(i));
            chatingRoomItem.setSelector(false);

            chatingRoomItems.add(chatingRoomItem);
        }

        return chatingRoomItems;
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
        String desc = "true";
        String excludeMyself = null;
        String sortKey = null;

        //聊天室列表
        String request = HttpRequest.postConversationQueryList(name, selectedStars, ownerId, adminUserId, personId, status,
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
        chatingRoomItemList.clear();

        chatingRoomItemList.addAll(setDefault(list));
        srlBaseHttpRecycler.finishRefresh();
        srlBaseHttpRecycler.setLoadmoreFinished(false);

        setList(chatingRoomItemList);
    }

    @Override
    public void noMorePagingList() {
        showShortToast(R.string.noMoreData);
        srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();
    }

    @Override
    public void loadMoreSuccessPagingList(List<ChatingRoom> list) {
        chatingRoomItemList.addAll(setDefault(list));
        srlBaseHttpRecycler.finishLoadmore();

        setList(chatingRoomItemList);

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

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public void Success(String url, int RequestCode, EntityBase entityBase) {
        super.Success(url, RequestCode, entityBase);
        switch (RequestCode) {
            case 1:
                showShortToast(R.string.associateConversationSuccess);

                Intent intentVote = new Intent(
                        BroadcastAction.ACTION_VOTE_REFRESH);
                // 发送广播
                sendBroadcast(intentVote);
                finish();
                break;
            case 2:
                showShortToast(R.string.associateConversationSuccess);

                Intent intentSupport = new Intent(
                        BroadcastAction.ACTION_SUPPORT_REFRESH);
                // 发送广播
                sendBroadcast(intentSupport);
                finish();
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }
}
