package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.home.FansMainActivity;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.fragment.campaign.DetailedConversationFragment;
import com.qingye.wtsyou.manager.HttpManager;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.model.EntityVoteDetailed;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.DateUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.widget.CircleProgress;
import com.qingye.wtsyou.widget.ObservableScrollView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.widget.CustomDialog;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_1;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_2;
import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class VoteDetailedActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivShare;
    //活动
    private ImageView ivBackground;
    private ImageView ivImgIcon;
    private TextView tvName;
    private TextView tvJoin;
    private TextView tvAll;
    private TextView tvTag;
    private TextView tvAddress;
    private TextView tvTime;
    private TextView tvDeadline;
    private TextView tvDescription;
    //发起人
    private TextView tvCreatorName;
    private ImageView ivImg;
    private TextView tvFollow;
    private TextView tvFans;
    private TextView tvFocus;
    private TextView tvCancelFocus;
    private RelativeLayout rlCreator;

    private Button btnVote,btnVoteEnd;
    private LinearLayout llMore;

    private CircleProgress mcircleProgressBar;

    private LinearLayout llHead;
    private ObservableScrollView scrollView;
    private ImageView backImageView;
    private int imageHeight;

    private RelativeLayout llConversation;
    private LinearLayout llAssociationConversation;

    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    private String uuid;
    private EntityVoteDetailed entityVoteDetailed;
    private List<ChatingRoom> chatingRoomList = new ArrayList<>();

    private HttpModel<EntityVoteDetailed> mDetailedHttpModel;

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, String uuid) {
        return new Intent(context, VoteDetailedActivity.class).putExtra(Constant.UUID, uuid);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastAction.ACTION_VOTE_REFRESH)) {
                refresh();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_detail,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        intent = getIntent();
        uuid = intent.getStringExtra(Constant.UUID);

        //获取详情
        mDetailedHttpModel = new HttpModel<>(EntityVoteDetailed.class);
        refresh();

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BroadcastAction.ACTION_VOTE_REFRESH);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        swipeRefresh = findView(R.id.swipe_refresh_widget);

        ivBack = findView(R.id.iv_left);
        ivShare = findView(R.id.iv_right);
        //活动
        //背景
        ivBackground = findView(R.id.iv_campaign_background_img);
        //小图
        ivImgIcon = findView(R.id.iv_campaign_img);
        //活动名称
        tvName = findView(R.id.tv_campaign_name);
        //参与人数
        tvJoin = findView(R.id.tv_campaign_value);
        //目标人数
        tvAll = findView(R.id.tv_campaign_all_value);
        //标签
        tvTag = findView(R.id.tv_tag);
        //地址
        tvAddress = findView(R.id.tv_campaign_place);
        //活动时间
        tvTime = findView(R.id.tv_campaign_time);
        //截止时间
        tvDeadline = findView(R.id.tv_campaign_end_time);
        //项目简介
        tvDescription = findView(R.id.tv_crowd_content);

        //发起人名字
        tvCreatorName = findView(R.id.tv_creator_name);
        //头像
        ivImg = findView(R.id.iv_creator);
        //关注
        tvFollow = findView(R.id.tv_focus_num);
        //粉丝
        tvFans = findView(R.id.tv_fans_num);
        //是否关注
        tvFocus = findView(R.id.tv_focus);
        tvCancelFocus = findView(R.id.tv_cancel_focus);
        rlCreator = findView(R.id.rlCreator);

        btnVote = findView(R.id.btn_vote);
        btnVoteEnd = findView(R.id.btn_vote_end);
        llMore = findView(R.id.ll_detailed_more);

        mcircleProgressBar = findView(R.id.join_progressbar);
        mcircleProgressBar.setPercentColor(R.color.black_text1);

        llHead = findView(R.id.ll_head);
        backImageView = findView(R.id.iv_campaign_background_img);
        scrollView = findView(R.id.scrollview);
        initListeners();

        //聊天室
        llConversation = findView(R.id.ll_conversation);
        llAssociationConversation = findView(R.id.ll_select_conversation);

    }

    private void initListeners() {
        // 获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = backImageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                backImageView.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = backImageView.getHeight();
                scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
                    @Override
                    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                        // TODO Auto-generated method stub
                        if (y <= 0) {
                            llHead.setBackgroundColor(Color.parseColor("#00ffffff"));
                            ivBack.setImageResource(R.mipmap.back_l);
                            ivShare.setImageResource(R.mipmap.share_w);
                        } else if (y > 0 && y <= imageHeight) {
                            llHead.setBackgroundColor(Color.parseColor("#ddffffff"));
                            ivBack.setImageResource(R.mipmap.back_a);
                            ivShare.setImageResource(R.mipmap.share_g);
                        }
                    }
                });

            }
        });


    }

    @Override
    public void initData() {
        //活动
        //模糊背景图
        String urlBackground = entityVoteDetailed.getContent().getActivityPic();
        Glide.with(context)
                .load(urlBackground)
                .apply(bitmapTransform(new BlurTransformation(25)))
                .into(ivBackground);
        //小图
        String urlImg = entityVoteDetailed.getContent().getActivityIcon();
        Glide.with(context)
                .load(urlImg)
                .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(ivImgIcon);
        //活动名称
        tvName.setText(entityVoteDetailed.getContent().getActivityName());
        //参与人数
        BigDecimal joinBig = entityVoteDetailed.getContent().getVotedNumber();
        double joinDou = joinBig.doubleValue();
        int joinInt = (int) joinDou;
        tvJoin.setText(Integer.toString(joinInt));
        //目标人数
        BigDecimal allBig = entityVoteDetailed.getContent().getSettingGoals();
        double allDou = allBig.doubleValue();
        int allInt = (int) allDou;
        tvAll.setText(Integer.toString(allInt));

        int progressValueInt = 0;
        if (allDou > 0) {
            //进度条
            BigDecimal progressValueBig = new BigDecimal(joinDou/allDou);
            String progressValueStr = StringUtil.getPrice(progressValueBig);
            progressValueInt = StringUtil.stringToInt(progressValueStr);
        }


        mcircleProgressBar.setMaxProgress(100);
        mcircleProgressBar.updateProgress(progressValueInt,700);
        //标签
        String state = entityVoteDetailed.getContent().getState();
        if (state.equals("votesuccess")) {
            tvTag.setText("投票成功");
            tvTag.setBackground(getResources().getDrawable(R.drawable.corners_gradient_gray));
            btnVote.setVisibility(View.GONE);
            btnVoteEnd.setVisibility(View.VISIBLE);
        }
        else if (state.equals("votefail")) {
            tvTag.setText("投票失败");
            tvTag.setBackground(getResources().getDrawable(R.drawable.corners_gradient_gray));
            btnVote.setVisibility(View.GONE);
            btnVoteEnd.setVisibility(View.VISIBLE);
        }
        else if (state.equals("voting")) {
            tvTag.setText("投票中");
            tvTag.setBackground(getResources().getDrawable(R.drawable.corners_gradient_red));
            btnVote.setVisibility(View.VISIBLE);
            btnVoteEnd.setVisibility(View.GONE);
        }
        //地址
        String address = entityVoteDetailed.getContent().getAddress().getPcdt().getProvince().toString()
                + entityVoteDetailed.getContent().getAddress().getPcdt().getCity().toString();
        tvAddress.setText(address);
        //活动时间
        tvTime.setText(DateUtil.resverDate(entityVoteDetailed.getContent().getStartTimeStr(),DATE_PATTERN_2,DATE_PATTERN_1));
        //截止时间
        tvDeadline.setText(DateUtil.resverDate(entityVoteDetailed.getContent().getDeadlineStr(),DATE_PATTERN_2,DATE_PATTERN_1));
        //项目简介
        tvDescription.setText(entityVoteDetailed.getContent().getDescription());

        //发起人姓名
        tvCreatorName.setText(entityVoteDetailed.getContent().getCreateUserName());
        //头像
        String headUrl = entityVoteDetailed.getContent().getCreateUserPic();
        if (headUrl != null) {
            Glide.with(context)
                    .load(headUrl)
                    .into(ivImg);
        }
        //关注
        tvFollow.setText("" + entityVoteDetailed.getContent().getFollowNumber());
        //粉丝
        tvFans.setText("" + entityVoteDetailed.getContent().getFunsNumber());
        if (entityVoteDetailed.getContent().getFollowCreatetUser()) {
            tvFocus.setVisibility(View.GONE);
            tvCancelFocus.setVisibility(View.VISIBLE);
        } else {
            tvFocus.setVisibility(View.VISIBLE);
            tvCancelFocus.setVisibility(View.GONE);
        }

        if (entityVoteDetailed.getContent().getChartRoomList() == null || entityVoteDetailed.getContent().getChartRoomList().isEmpty()) {
            String currentUserId = HttpManager.getInstance().getUserId();
            if (currentUserId.equals(entityVoteDetailed.getContent().getCreateUserId())) {
                llAssociationConversation.setVisibility(View.VISIBLE);
                llConversation.setVisibility(View.VISIBLE);
            } else {
                llConversation.setVisibility(View.GONE);
            }

        } else {
            llConversation.setVisibility(View.VISIBLE);
            chatingRoomList = entityVoteDetailed.getContent().getChartRoomList();
            //注意这里是调用getSupportFragmentManager()方法
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            DetailedConversationFragment campaignDetailedConversationFragment = new DetailedConversationFragment();
            if (chatingRoomList.size() > 0) {
                Bundle bundleConversation = new Bundle();
                bundleConversation.putSerializable(Constant.CHATINTROOMLIST, (Serializable) chatingRoomList);
                campaignDetailedConversationFragment.setArguments(bundleConversation);
                transaction.replace(R.id.list_conversation,campaignDetailedConversationFragment);

                transaction.commit();
            }
        }

    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver);

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
                        refresh();
                        showShortToast(R.string.getSuccess);
                        swipeRefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });
        swipeRefresh.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                return scrollView.getScrollY() > 0;
            }
        });
    }

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        btnVote.setOnClickListener(this);
        llMore.setOnClickListener(this);
        tvFocus.setOnClickListener(this);
        tvCancelFocus.setOnClickListener(this);
        rlCreator.setOnClickListener(this);
        llAssociationConversation.setOnClickListener(this);
    }

    public void share() {
        final String url ="http://mobile.umeng.com/social";
        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(VoteDetailedActivity.this).setDisplayList(
                SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .addButton("复制链接", "复制链接", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mShowWord.equals("复制链接")) {
                            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            //创建ClipData对象
                            ClipData clipData = ClipData.newPlainText("simple text copy", url);
                            //添加ClipData对象到剪切板中
                            clipboardManager.setPrimaryClip(clipData);
                            Toast.makeText(VoteDetailedActivity.this, "复制链接成功", Toast.LENGTH_LONG).show();

                        } else {
                            UMWeb web = new UMWeb(url);
                            web.setTitle("来自分享面板标题");
                            web.setDescription("来自分享面板内容");
                            web.setThumb(new UMImage(VoteDetailedActivity.this, R.mipmap.syp_icon));
                            new ShareAction(VoteDetailedActivity.this).withMedia(web)
                                    .setPlatform(share_media)
                                    .setCallback(mShareListener)
                                    .share();
                        }
                    }
                });
    }

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<VoteDetailedActivity> mActivity;

        private CustomShareListener(VoteDetailedActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                ShareBoardConfig config = new ShareBoardConfig();
                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                config.setShareboardBackgroundColor(Color.WHITE);
                share();
                mShareAction.open(config);
                break;
            case R.id.btn_vote:
                vote();
                break;
            case R.id.ll_detailed_more:
                toActivity(DetailedActivity.createIntent(context, "项目详情",
                        entityVoteDetailed.getContent().getDetail()));
                break;
            case R.id.tv_focus:
                focusFans(entityVoteDetailed.getContent().getCreateUserId(),tvFocus,tvCancelFocus);
                break;
            case R.id.tv_cancel:
                cancelFans(entityVoteDetailed.getContent().getCreateUserId(),tvFocus,tvCancelFocus);
                break;
            case R.id.rlCreator:
                toActivity(FansMainActivity.createIntent(context, entityVoteDetailed.getContent().getCreateUserId()));
                break;
            case R.id.ll_select_conversation:
                String selectStars = entityVoteDetailed.getContent().getRelevanceStar();
                String[] relevanceStar = new String[1];
                for (int i = 0;i < 1;i ++) {
                    relevanceStar[i] = selectStars;
                }
                toActivity(CreatorAssociateConversationActivity.createIntent(context, relevanceStar, "vote",
                        entityVoteDetailed.getContent().getActivityId()));
                break;
            default:
                break;
        }
    }

    public void focusFans(String userId, final TextView focus, final TextView can) {

        if (NetUtil.checkNetwork(getActivity())) {
            setProgressBar();
            progressBar.show();

            HttpRequest.getGetFocusFriend(0, userId, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  zuo.biao.library.util.JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功
                            showShortToast(R.string.focusSuccess);

                            focus.setVisibility(View.GONE);
                            can.setVisibility(View.VISIBLE);

                            progressBarDismiss();

                        }else{//显示失败信息

                            showShortToast(entityBase.getMessage());
                            progressBarDismiss();
                        }
                    }else{
                        showShortToast(R.string.noReturn);

                        progressBarDismiss();
                    }
                }
            });

        } else {
            showProgressDialog(R.string.checkNetwork);
        }
    }

    public void cancelFans(String userId, final TextView focus, final TextView can) {

        if (NetUtil.checkNetwork(context)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.getGetCancelFocusFriend(0, userId, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  zuo.biao.library.util.JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功
                            showShortToast(R.string.cancelFocusSuccess);

                            focus.setVisibility(View.VISIBLE);
                            can.setVisibility(View.GONE);

                            Intent intent = new Intent(
                                    BroadcastAction.ACTION_FRIEND_REFRESH);
                            // 发送广播
                            sendBroadcast(intent);

                            progressBarDismiss();

                        }else{//显示失败信息

                            showShortToast(entityBase.getMessage());
                            progressBarDismiss();
                        }
                    }else{
                        showShortToast(R.string.noReturn);

                        progressBarDismiss();
                    }
                }
            });

        } else {
            showProgressDialog(R.string.checkNetwork);
        }
    }

    public void vote() {

        String uuid = entityVoteDetailed.getContent().getActivityId();
        //检查网络
        if (NetUtil.checkNetwork(this)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.getVote(0, uuid, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功//showShortToast(R.string.getSuccess);
                            showShortToast(R.string.voteSuccess);

                            Intent intent1 = new Intent(
                                    BroadcastAction.ACTION_VOTE_REFRESH);
                            // 发送广播
                            sendBroadcast(intent1);

                            Intent intent2 = new Intent(
                                    BroadcastAction.ACTION_SHOWALL_REFRESH);
                            // 发送广播
                            sendBroadcast(intent2);

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
                        progressBarDismiss();

                        showShortToast(R.string.noReturn);
                    }
                }
            });
        } else {
            showShortToast(R.string.checkNetwork);
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

    public void refresh() {

        setProgressBar();
        progressBar.show();

        mDetailedHttpModel.get(uuid,URL_BASE + URLConstant.VOTEDETIALED,1,this);
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
                entityVoteDetailed = mDetailedHttpModel.getData();
                initData();
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }
}
