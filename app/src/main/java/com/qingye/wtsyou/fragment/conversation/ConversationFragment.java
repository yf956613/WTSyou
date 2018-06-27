package com.qingye.wtsyou.fragment.conversation;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.conversation.CollectionChatRoomActivity;
import com.qingye.wtsyou.activity.conversation.CreateConversationActivity;
import com.qingye.wtsyou.activity.my.MyConversationActivity;
import com.qingye.wtsyou.activity.search.SearchConversationActivity;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.EntityPersonalMessage;
import com.qingye.wtsyou.utils.URLConstant;

import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {

    private TextView tvHead;
    private ImageView ivSearch,ivCreate;
    private ImageView ivMy,ivCollection;

    private ScrollView scrollView;
    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    private EntityPersonalMessage entityPersonalMessage;

    private HttpModel<EntityPersonalMessage> mEntityPersonalMessageHttpModel;

    /*private HttpModel<EntityPageData> mEntityPageDataHttpModel;

    private List<ChatingRoom> chatingRoomList = new ArrayList<>();*/

    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_conversation);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //获取个人信息
        mEntityPersonalMessageHttpModel = new HttpModel<>(EntityPersonalMessage.class);

        //聊天室列表
        /*mEntityPageDataHttpModel = new HttpModel<>(EntityPageData.class);
        conversationQuery();*/

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        return view;
    }

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static ConversationFragment createInstance() {
        return new ConversationFragment();
    }

    @Override
    public void initView() {
        scrollView = findView(R.id.scrollview);
        swipeRefresh = findView(R.id.swipe_refresh_widget);

        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("聊天室");
        ivSearch = findView(R.id.iv_left);
        ivSearch.setImageResource(R.mipmap.search_b);
        ivCreate = findView(R.id.iv_right);
        ivCreate.setImageResource(R.mipmap.create);
        ivMy =  findView(R.id.iv_my);
        ivCollection = findView(R.id.iv_collection);

        fragment();
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

    }

    @Override
    public void initEvent() {
        ivSearch.setOnClickListener(this);
        ivCreate.setOnClickListener(this);
        ivMy.setOnClickListener(this);
        ivCollection.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                toActivity(SearchConversationActivity.createIntent(context));
                break;
            case R.id.iv_right:
                getPersonalMessage();
                break;
            case R.id.iv_my:
                toActivity(MyConversationActivity.createIntent(context));
                break;
            case R.id.iv_collection:
                toActivity(CollectionChatRoomActivity.createIntent(context));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }

    //聊天室
    private void fragment() {

        setProgressBar();
        progressBar.show();

        //注意这里是调用getChildFragmentManager()方法
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        //置顶聊天室
        ConversationTopFragment conversationTopFragment = new ConversationTopFragment();
        /*if (topRecommendList.size() > 0) {
            Bundle bundleTop = new Bundle();
            bundleTop.putSerializable(Constant.TOPRECOMMEND, (Serializable) topRecommendList);
            conversationTopFragment.setArguments(bundleTop);
            transaction.replace(R.id.list_top,conversationTopFragment);
        }*/
        transaction.replace(R.id.list_top,conversationTopFragment);
        //热门聊天室
        ConversationHotFragment conversationHotFragment = new ConversationHotFragment();
        /*if (hotRecommendList.size() > 0) {
            Bundle bundleHot = new Bundle();
            bundleHot.putSerializable(Constant.CONVERSATIONLIST, (Serializable) hotRecommendList);
            conversationHotFragment.setArguments(bundleHot);
            transaction.replace(R.id.list_hot,conversationHotFragment);
        }*/
        transaction.replace(R.id.list_hot,conversationHotFragment);
        transaction.commitAllowingStateLoss();

        progressBarDismiss();
    }

    /*public void conversationQuery() {
        setProgressBar();
        progressBar.show();

        String name = null;
        String ownerId = null;
        String adminUserId = null;
        String personId = null;
        String status = null;
        String topRecommend = null;
        String hotRecommend = null;
        String desc = "false";
        String excludeMyself = null;
        //聊天室列表
        String request = HttpRequest.postConversationQueryList(name, null, ownerId, adminUserId, personId, status,
                topRecommend,hotRecommend, 1, 10, desc, excludeMyself);
        mEntityPageDataHttpModel.post(request, URL_BASE + URLConstant.CONVERSATIONQUERY,1,this);
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
                EntityPageData.Content entityPageData = mEntityPageDataHttpModel.getData().getContent();
                chatingRoomList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityPageData.getData())
                        ,new TypeToken<List<ChatingRoom>>(){}.getType());
                List<ChatingRoom> topRecommendList = new ArrayList<>();
                List<ChatingRoom> hotRecommendList = new ArrayList<>();
                for (ChatingRoom chatingRoom : chatingRoomList) {
                    if (chatingRoom.isTopRecommend()) {
                        topRecommendList.add(chatingRoom);
                    }
                    if (chatingRoom.isHotRecommend()) {
                        hotRecommendList.add(chatingRoom);
                    }
                }
                fragment(topRecommendList, hotRecommendList);
                fragment(chatingRoomList,chatingRoomList);
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }*/

    public void getPersonalMessage() {
        /*setProgressBar();
        progressBar.show();*/

        mEntityPersonalMessageHttpModel.get(URL_BASE + URLConstant.GETPERSONALMESSAGE,1,this);
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
                entityPersonalMessage = mEntityPersonalMessageHttpModel.getData();
                if (entityPersonalMessage.getContent().getLevel() > 2) {
                    toActivity(CreateConversationActivity.createIntent(context));
                } else {
                    showShortToast(R.string.createChatRoomFail);
                }
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }
}
