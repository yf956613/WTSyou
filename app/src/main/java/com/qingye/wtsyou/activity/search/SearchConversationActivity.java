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
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.campaign.ConversationAdapter;
import com.qingye.wtsyou.adapter.search.HotTopicAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.fragment.conversation.SearchRecommendConversationFragment;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.HotTopic;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.SPUtil;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.campaign.ConversationView;
import com.qingye.wtsyou.widget.AutoLineFeedLayout;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;
import com.qingye.wtsyou.widget.VpSwipeRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.CacheCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpPageCallBack;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class SearchConversationActivity extends BaseHttpRecyclerActivity<ChatingRoom,ConversationView,ConversationAdapter>
        implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener, CacheCallBack<ChatingRoom>,
        OnHttpPageCallBack<EntityPageData,ChatingRoom> {

    private EditText edtContent;
    private RelativeLayout rlCancel;
    private ImageView ivDeleteAll;
    private AutoLineFeedLayout layout;

    private LinearLayout llHistory,llHotTopic,llRecommend,llResult;

    private ScrollView scrollView;
    private VpSwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    private HttpModel<EntityPageData> mHotTopicHttpModel;
    private HttpPageModel<EntityPageData,ChatingRoom> mChatingRoomHttpModel;

    //热门话题
    private RecyclerView rclHotTopic;
    private HotTopicAdapter hotTopicAdapter;
    private List<HotTopic> hotTopicList = new ArrayList<>();

    private List<ChatingRoom> chatingRoomList = new ArrayList<>();

    List<String> history = new ArrayList<>();
    private boolean SP = true;
    Gson gson = new Gson();

    private int count = 0;

    private String keyWords = "";

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, SearchConversationActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_conversation,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //热门话题
        mHotTopicHttpModel = new HttpModel<>(EntityPageData.class);
        getHotTopic();
        //聊天室列表
        mChatingRoomHttpModel = new HttpPageModel<>(EntityPageData.class);

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
        srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(true);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(true);//尾部
    }

    @Override
    public void initView() {
        super.initView();
        SP = SPUtil.contains(this, Constant.CONVERSATIONSEARCHHISTORY);
        if(SP == true) {
            String str = (String) SPUtil.get(this, Constant.CONVERSATIONSEARCHHISTORY, "");
            if(str != null){
                history = gson.fromJson(str, new TypeToken<List<String>>(){}.getType());
            }
        }

        scrollView = findView(R.id.scrollview);
        swipeRefresh = findView(R.id.swipe_refresh_widget);

        rlCancel = findView(R.id.rl_cancel);
        ivDeleteAll = findView(R.id.iv_delete_all);
        layout = findView(R.id.al);

        setHistoryBtn();

        llHistory = findView(R.id.llHistory);
        llHotTopic = findView(R.id.llHotTopic);
        llRecommend = findView(R.id.llRecommend);
        llResult = findView(R.id.llResult);

        edtContent = findView(R.id.edit_search_content);
        edtContent.setHint("搜索聊天室");
        if (edtContent.getTag() instanceof TextWatcher) {
            edtContent.removeTextChangedListener((TextWatcher) edtContent.getTag());
        }
        TextWatcher watcher = new TextWatcher() {
            private CharSequence temp ;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s ;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {

                } else {
                    llHistory.setVisibility(View.VISIBLE);
                    llHotTopic.setVisibility(View.VISIBLE);
                    llRecommend.setVisibility(View.VISIBLE);
                    llResult.setVisibility(View.GONE);
                }
            }
        };
        edtContent.addTextChangedListener(watcher);
        edtContent.setTag(watcher);
        edtContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_SEND
                        || keyCode == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    // do some your things
                    llHistory.setVisibility(View.GONE);
                    llHotTopic.setVisibility(View.GONE);
                    llRecommend.setVisibility(View.GONE);
                    llResult.setVisibility(View.VISIBLE);

                    if (edtContent.getText().toString().isEmpty()) {
                        showShortToast(R.string.noContent);
                    } else {
                        if (history.size() > 0) {
                            for (int i = 0;i < history.size();i ++) {
                                if (history.get(i).equals(edtContent.getText().toString().trim())) {
                                    count = 0;
                                    break;
                                } else {
                                    count = count + 1;
                                }
                            }
                        } else {
                            count = count + 1;
                        }

                        if (count != 0) {
                            history.add(edtContent.getText().toString().trim());
                            setHistoryBtn();
                        }

                        keyWords = edtContent.getText().toString().trim();
                        conversationQuery();
                        add();
                    }
                }
                return false;
            }
        });

        //搜索结果
        rclHotTopic = findView(R.id.list_hot_topic);
        //实例化一个GridLayoutManager，列数为2
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rclHotTopic.setLayoutManager(layoutManager);
        hotTopicAdapter = new HotTopicAdapter(hotTopicList, this);
        rclHotTopic.setAdapter(hotTopicAdapter);
        hotTopicAdapter.setOnItemClick(new HotTopicAdapter.OnItemClick() {

            @Override
            public void ItemClick(View v, int position) {
                if (history.size() > 0) {
                    for (int i = 0;i < history.size();i ++) {
                        if (history.get(i).equals(hotTopicList.get(position).getTitle())) {
                            count = 0;
                            break;
                        } else {
                            count = count + 1;
                        }
                    }
                } else {
                    count = count + 1;
                }

                if (count != 0) {
                    history.add(hotTopicList.get(position).getTitle());
                    setHistoryBtn();
                }

                edtContent.setText(hotTopicList.get(position).getTitle());

                keyWords = edtContent.getText().toString().trim();
                conversationQuery();
                add();
            }
        });

        fragment();
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
                        //conversationQuery();
                        count = 0;
                        conversationQuery();
                        fragment();
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

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void getListAsync(int page) {

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

    @Override
    public void initEvent() {
        super.initEvent();
        rlCancel.setOnClickListener(this);
        ivDeleteAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_cancel:
                if (history.size() > 0) {
                    add();
                } else {
                    delete();
                }

                finish();
                break;
            case R.id.iv_delete_all:
                layout.removeAllViews();
                delete();
                break;
            default:
                break;
        }
    }

    public void setHistoryBtn() {
        View view;
        layout.removeAllViews();
        for (int i = 0; i < history.size(); i ++) {
            final int position = i;
            view = LayoutInflater.from(SearchConversationActivity.this).inflate(R.layout.list_delete_search, null);
            final TextView button = (TextView) view.findViewById(R.id.btn);
            final ImageView img = (ImageView) view.findViewById(R.id.iv_delete);
            button.setText(history.get(i));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llHistory.setVisibility(View.GONE);
                    llHotTopic.setVisibility(View.GONE);
                    llRecommend.setVisibility(View.GONE);
                    llResult.setVisibility(View.VISIBLE);

                    edtContent.setText(history.get(position));

                    keyWords = edtContent.getText().toString().trim();
                    conversationQuery();
                }
            });
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    history.remove(position);
                    layout.removeViewAt(position);
                }
            });

            layout.addView(view, i);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

    public void add() {
        //利用Gson转化成Json Array数据
        String jsonStr = gson.toJson(history);
        SPUtil.put(this,Constant.CONVERSATIONSEARCHHISTORY, jsonStr);
    }

    public void delete() {
        SPUtil.remove(this,Constant.CONVERSATIONSEARCHHISTORY);
        history.clear();
        /*historyStarsSearchAdapter.notifyDataSetChanged();*/
    }

    public void fragment() {
        setProgressBar();
        progressBar.show();

        SearchRecommendConversationFragment searchRecommendConversationFragment= new SearchRecommendConversationFragment();
        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_hot_conversation,searchRecommendConversationFragment);
        transaction.commitAllowingStateLoss();

        progressBarDismiss();
    }

    public void getHotTopic() {
        /*setProgressBar();
        progressBar.show();*/

        //热门列表
        mHotTopicHttpModel.get( URL_BASE + URLConstant.CONVERSATIONHOTTOPIC, 1,this);
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
                EntityPageData.Content entityPageData =  mHotTopicHttpModel.getData().getContent();
                List<HotTopic> hotTopics = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageData.getData())
                        ,new TypeToken<List<HotTopic>>(){}.getType());
                if (hotTopics.size() < 8) {
                    hotTopicList.addAll(hotTopics);
                } else {
                    for(int i = 0; i < 8;i ++) {
                        hotTopicList.add(hotTopics.get(i));
                    }
                }
                hotTopicAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void conversationQuery() {

        mChatingRoomHttpModel.refreshPost( URL_BASE + URLConstant.CONVERSATIONQUERY,this);
    }

    @Override
    public List<ChatingRoom> getList(EntityPageData data) {
        return GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(data.getContent().getData())
                ,new TypeToken<List<ChatingRoom>>(){}.getType());
    }

    @Override
    public String getRequestJsonStr(int page, int pageSize) {

        String name = keyWords;
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
        mChatingRoomHttpModel.refreshPost(URL_BASE + URLConstant.CONVERSATIONQUERY, this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        //聊天室列表
        mChatingRoomHttpModel.loadMorePost(URL_BASE + URLConstant.CONVERSATIONQUERY, this);
    }
}
