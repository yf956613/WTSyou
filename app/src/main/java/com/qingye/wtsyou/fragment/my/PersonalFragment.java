package com.qingye.wtsyou.fragment.my;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.conversation.MyConversationActivity;
import com.qingye.wtsyou.activity.my.CampaignActivity;
import com.qingye.wtsyou.activity.my.FocusStarsActivity;
import com.qingye.wtsyou.activity.my.AddressActivity;
import com.qingye.wtsyou.activity.my.CardActivity;
import com.qingye.wtsyou.activity.my.CoinActivity;
import com.qingye.wtsyou.activity.my.DataActivity;
import com.qingye.wtsyou.activity.my.DiamondActivity;
import com.qingye.wtsyou.activity.my.FriendsActivity;
import com.qingye.wtsyou.activity.my.HeartActivity;
import com.qingye.wtsyou.activity.my.MessageActivity;
import com.qingye.wtsyou.activity.my.OrderActivity;
import com.qingye.wtsyou.activity.my.SettingActivity;
import com.qingye.wtsyou.activity.my.SignInActivity;
import com.qingye.wtsyou.activity.my.TicketActivity;
import com.qingye.wtsyou.modle.EntityPersonalMessage;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.widget.CustomDialog;

import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {

    private ImageView ivLeft;
    private TextView tvRight;

    private RelativeLayout rlPersonalMessage;

    private TextView tvNickName;
    private TextView tvSignature;
    private ImageView ivHead;
    private ImageView ivBackground;

    private LinearLayout llIdol;
    private LinearLayout llFriends;
    private LinearLayout llConversation;
    private LinearLayout llHeart;
    private LinearLayout llDiamonds;
    private LinearLayout llCoin;
    private RelativeLayout llCampaign;
    private RelativeLayout llOrder;
    private RelativeLayout llCard;
    private RelativeLayout llAddress;
    private RelativeLayout llTicket;
    private RelativeLayout llSet;

    private ScrollView scrollView;
    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    private EntityPersonalMessage entityPersonalMessage;
    private int heartCount,diamondCount,coinCount;

    public PersonalFragment() {
        // Required empty public constructor
    }

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static PersonalFragment createInstance() {
        return new PersonalFragment();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastAction.ACTION_MESSAGE_REFRESH)) {
                getPersonalMessage();
                progressBarDismiss();
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_personal);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BroadcastAction.ACTION_MESSAGE_REFRESH);
        // 注册广播
        context.registerReceiver(mBroadcastReceiver, myIntentFilter);
        getPersonalMessage();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        return view;
    }

    @Override
    public void initView() {
        scrollView = findViewById(R.id.scrollview);
        swipeRefresh = findViewById(R.id.swipe_refresh_widget);

        ivLeft = findViewById(R.id.iv_left);
        tvRight = findViewById(R.id.tv_right);

        rlPersonalMessage = findViewById(R.id.rlPersonalMessage);

        //昵称
        tvNickName = findViewById(R.id.personal_name);
        //头像
        ivHead = findViewById(R.id.iv_personal);
        //签名
        tvSignature = findViewById(R.id.personal_signature);
        //背景
        ivBackground = findViewById(R.id.iv_background);

        llIdol = findViewById(R.id.llIdol);
        llFriends = findViewById(R.id.llFriends);
        llConversation = findViewById(R.id.llConversation);
        llHeart = findViewById(R.id.llHeart);
        llDiamonds = findViewById(R.id.llDiamonds);
        llCoin = findViewById(R.id.llCoin);
        llCampaign = findViewById(R.id.llCampaign);
        llOrder = findViewById(R.id.llOrder);
        llCard = findViewById(R.id.llCard);
        llAddress = findViewById(R.id.llAddress);
        llTicket = findViewById(R.id.llTicket);
        llSet = findViewById(R.id.llSet);
    }

    public void onResume() {
        //getPersonalMessage();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        context.unregisterReceiver(mBroadcastReceiver);

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
                        getPersonalMessage();
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
    public void initData() {
        //昵称
        tvNickName.setText(entityPersonalMessage.getContent().getNickname());
        //头像
        if (entityPersonalMessage.getContent().getPhoto() != null) {
            String url = entityPersonalMessage.getContent().getPhoto();
            Glide.with(context)
                    .load(url)
                    .into(ivHead);
        }
        //背景
        if (entityPersonalMessage.getContent().getPhoto() != null) {
            String url = entityPersonalMessage.getContent().getBackgruoud();
            Glide.with(context)
                    .load(url)
                    .into(ivBackground);
        }
        //签名
        if (entityPersonalMessage.getContent().getAutograph() != null) {
            tvSignature.setText(entityPersonalMessage.getContent().getAutograph());
        }

        heartCount = entityPersonalMessage.getContent().getLoveCount();
        diamondCount = entityPersonalMessage.getContent().getDiamondCount();
        coinCount = entityPersonalMessage.getContent().getGoldCount();
    }

    @Override
    public void initEvent() {
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);

        rlPersonalMessage.setOnClickListener(this);

        llIdol.setOnClickListener(this);
        llFriends.setOnClickListener(this);
        llConversation.setOnClickListener(this);
        llHeart.setOnClickListener(this);
        llDiamonds.setOnClickListener(this);
        llCoin.setOnClickListener(this);
        llCampaign.setOnClickListener(this);
        llOrder.setOnClickListener(this);
        llCard.setOnClickListener(this);
        llAddress.setOnClickListener(this);
        llTicket.setOnClickListener(this);
        llSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlPersonalMessage:
                toActivity(DataActivity.createIntent(context,entityPersonalMessage));
                break;
            case R.id.iv_left:
                toActivity(MessageActivity.createIntent(context));
                break;
            case R.id.tv_right:
                toActivity(SignInActivity.createIntent(context));
                break;
            case R.id.llIdol:
                toActivity(FocusStarsActivity.createIntent(context));
                break;
            case R.id.llFriends:
                toActivity(FriendsActivity.createIntent(context));
                break;
            case R.id.llConversation:
                toActivity(MyConversationActivity.createIntent(context));
                break;
            case R.id.llHeart:
                toActivity(HeartActivity.createIntent(context,heartCount));
                break;
            case R.id.llDiamonds:
                toActivity(DiamondActivity.createIntent(context,diamondCount));
                break;
            case R.id.llCoin:
                toActivity(CoinActivity.createIntent(context,coinCount));
                break;
            case R.id.llCampaign:
                toActivity(CampaignActivity.createIntent(context));
                break;
            case R.id.llOrder:
                toActivity(OrderActivity.createIntent(context));
                break;
            case R.id.llCard:
                toActivity(CardActivity.createIntent(context));
                break;
            case R.id.llAddress:
                toActivity(AddressActivity.createIntent(context));
                break;
            case R.id.llTicket:
                toActivity(TicketActivity.createIntent(context));
                break;
            case R.id.llSet:
                toActivity(SettingActivity.createIntent(context));
                break;
            default:
                break;
        }
    }

    public void getPersonalMessage() {
        if (NetUtil.checkNetwork(getActivity())) {
            setProgressBar();
            progressBar.show();

            HttpRequest.getPersonalMessage(0, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        entityPersonalMessage =  JSON.parseObject(resultJson,EntityPersonalMessage.class);
                        if(entityPersonalMessage.isSuccess()){
                            //成功
                            //showShortToast(R.string.getSuccess);

                            initData();

                            progressBarDismiss();
                        }else{//显示失败信息
                            if (entityPersonalMessage.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityPersonalMessage.getMessage());
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

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }

}
