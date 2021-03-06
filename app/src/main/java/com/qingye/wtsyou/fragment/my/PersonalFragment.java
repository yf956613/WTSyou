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
import com.qingye.wtsyou.activity.my.AddressActivity;
import com.qingye.wtsyou.activity.my.CampaignActivity;
import com.qingye.wtsyou.activity.my.CardActivity;
import com.qingye.wtsyou.activity.my.CoinActivity;
import com.qingye.wtsyou.activity.my.DataActivity;
import com.qingye.wtsyou.activity.my.DiamondActivity;
import com.qingye.wtsyou.activity.my.FocusStarsActivity;
import com.qingye.wtsyou.activity.my.FriendsActivity;
import com.qingye.wtsyou.activity.my.HeartActivity;
import com.qingye.wtsyou.activity.my.MessageActivity;
import com.qingye.wtsyou.activity.my.MyConversationActivity;
import com.qingye.wtsyou.activity.my.OrderActivity;
import com.qingye.wtsyou.activity.my.SettingActivity;
import com.qingye.wtsyou.activity.my.SignInActivity;
import com.qingye.wtsyou.activity.my.TicketActivity;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.EntityPersonalMessage;
import com.qingye.wtsyou.utils.BroadcastAction;
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
public class PersonalFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {

    private ImageView ivLeft;
    private TextView tvRight;

    private RelativeLayout rlPersonalMessage;

    private TextView tvNickName;
    private TextView tvSignature;
    private ImageView ivHead;
    private ImageView ivBackground;
    private ImageView ivLvImg;//等级

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

    private int level;

    private HttpModel<EntityPersonalMessage> mEntityPersonalMessageHttpModel;

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

        //获取个人信息
        mEntityPersonalMessageHttpModel = new HttpModel<>(EntityPersonalMessage.class);

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BroadcastAction.ACTION_MESSAGE_REFRESH);
        // 注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
        getPersonalMessage();

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
        //等级
        ivLvImg = findView(R.id.iv_lv_img);

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
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getActivity().unregisterReceiver(mBroadcastReceiver);

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
        if (isAdded()) {
            //昵称
            tvNickName.setText(entityPersonalMessage.getContent().getNickname());
            //头像
            if (entityPersonalMessage.getContent().getPhoto() != null) {
                //图片
                String url = entityPersonalMessage.getContent().getPhoto();
                if (url != null) {
                    Glide.with(getActivity())
                            .load(url)
                            .into(ivHead);
                } else {
                    int defaultHead = R.mipmap.head;
                    Glide.with(context)
                            .load(defaultHead)
                            .into(ivHead);
                }

            }
            //背景
            if (entityPersonalMessage.getContent().getBackgruoud() != null) {
                String url = entityPersonalMessage.getContent().getBackgruoud();
                Glide.with(getActivity())
                        .load(url)
                        .into(ivBackground);
            }
            //签名
            if (entityPersonalMessage.getContent().getAutograph() != null) {
                tvSignature.setText(entityPersonalMessage.getContent().getAutograph());
            }

            //等级
            level = entityPersonalMessage.getContent().getLevel();
            levelPic(level);
        }

    }

    public void levelPic(int level) {
        switch (level) {
            case 1:
                ivLvImg.setImageResource(R.mipmap.lv1);
                break;
            case 2:
                ivLvImg.setImageResource(R.mipmap.lv2);
                break;
            case 3:
                ivLvImg.setImageResource(R.mipmap.lv3);
                break;
            case 4:
                ivLvImg.setImageResource(R.mipmap.lv4);
                break;
            case 5:
                ivLvImg.setImageResource(R.mipmap.lv5);
                break;
            case 6:
                ivLvImg.setImageResource(R.mipmap.lv6);
                break;
            case 7:
                ivLvImg.setImageResource(R.mipmap.lv7);
                break;
            case 8:
                ivLvImg.setImageResource(R.mipmap.lv8);
                break;
            case 9:
                ivLvImg.setImageResource(R.mipmap.lv9);
                break;
            case 10:
                ivLvImg.setImageResource(R.mipmap.lv10);
                break;
            default:
                break;
        }
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
                toActivity(HeartActivity.createIntent(context));
                break;
            case R.id.llDiamonds:
                toActivity(DiamondActivity.createIntent(context));
                break;
            case R.id.llCoin:
                toActivity(CoinActivity.createIntent(context));
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

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }

    public void getPersonalMessage() {
        setProgressBar();
        progressBar.show();

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
                initData();
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }

}
