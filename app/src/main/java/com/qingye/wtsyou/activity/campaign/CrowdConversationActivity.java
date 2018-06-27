package com.qingye.wtsyou.activity.campaign;

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
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.fragment.campaign.CrowdManageConversationFragment;
import com.qingye.wtsyou.fragment.campaign.OtherConversationFragment;
import com.qingye.wtsyou.fragment.conversation.CreateConversationFragment;
import com.qingye.wtsyou.manager.HttpManager;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.manager.HttpPageModel;
import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.widget.CustomDialog;

public class CrowdConversationActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener {

    private ImageView ivBack;
    private TextView tvHead;

    private String userId;

    private HttpModel<EntityPageData> mManagerConversationHttpModel;
    private HttpPageModel<EntityPageData, ChatingRoom> mOtherConversationHttpModel;

    FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);

    private List<ChatingRoom> otherConversation = new ArrayList<>();

    private ScrollView scrollView;
    private SwipeRefreshLayout swipeRefresh;
    private RelativeLayout linearLayout;
    private CustomDialog progressBar;

    private List<ChatingRoom> myChatingRooms =new ArrayList<>();
    private List<ChatingRoom> managerChatingRooms =new ArrayList<>();

    private String[] selectedStars = new String[3];
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, String[] selectStars) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(Constant.SELECTED_STARS, selectStars);
        return new Intent(context, CrowdConversationActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowd_conversation,this);

        userId = HttpManager.getInstance().getUserId();

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        intent = getIntent();
        //chatingRooms = (List<ChatingRoom>) intent.getSerializableExtra(Constant.CONVERSATIONLIST);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //setList(chatingRooms);
    }

    @Override
    public void initView() {
        swipeRefresh = findView(R.id.swipe_refresh_widget);
        linearLayout = findView(R.id.linearlayout);
        scrollView = findView(R.id.scrollview);

        ivBack = findView(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("聊天室列表");

        fragment();
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

                        fragment();
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

    }

    @Override
    public void initEvent() {
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    public void fragment() {
        Bundle bundleStars = new Bundle();
        bundleStars.putSerializable(Constant.SELECTED_STARS, selectedStars);
        CreateConversationFragment createConversationFragment = new CreateConversationFragment();
        CrowdManageConversationFragment manageConversationFragment = new CrowdManageConversationFragment();
        OtherConversationFragment otherConversationFragment = new OtherConversationFragment();

        createConversationFragment.setArguments(bundleStars);
        manageConversationFragment.setArguments(bundleStars);
        otherConversationFragment.setArguments(bundleStars);

        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.list_my,createConversationFragment);
        transaction.replace(R.id.list_manage,manageConversationFragment);
        transaction.replace(R.id.list_other,otherConversationFragment);


        transaction.commitAllowingStateLoss();
    }
}
