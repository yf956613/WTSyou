package com.qingye.wtsyou.activity.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.fragment.home.FansMainCampaignFragment;
import com.qingye.wtsyou.fragment.home.FansMainConversationFragment;
import com.qingye.wtsyou.fragment.home.FansMainIdolFragment;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.Activitys;
import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.model.EntityPageData;
import com.qingye.wtsyou.model.EntityPersonalHome;
import com.qingye.wtsyou.model.FansIdol;
import com.qingye.wtsyou.model.Member;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.widget.ObservableScrollView;
import com.qingye.wtsyou.widget.VpSwipeRefreshLayout;
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
import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class FansMainActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivMore;
    private ImageView ivPersonalImg;//个人头像
    private ImageView backImageView;//背景主页
    private TextView tvPersonalName;//昵称
    private TextView tvPersonalSign;//签名
    private TextView tvLove,tvNo;//爱心、贡献榜
    private ImageView ivLvImg;//等级

    private TextView tvFocus,tvCancelFocued;//关注、取消关注
    /*private LinearLayout llMutualFocus;//互相关注*/
    private TextView tvCancelMutual,tvTalk;//取消互相关注、私信

    private LinearLayout llHead;
    private ObservableScrollView scrollView;
    private int imageHeight;
    private VpSwipeRefreshLayout swipeRefresh;

    private CustomDialog progressBar;

    private HttpModel<EntityPersonalHome> mEntityPersonalHomeHttpModel;
    private HttpModel<EntityPageData> mEntityStarsHttpModel;
    private HttpModel<EntityPageData> mEntityConversationHttpModel;

    private Member member;
    private List<FansIdol> starsList = new ArrayList<>();
    private List<ChatingRoom> chatRoomsList = new ArrayList<>();
    private List<Activitys> activitiyList = new ArrayList<>();
    private int rank;
    private boolean isFollow;
    private int level;

    private String userId;

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, String userId) {
        return new Intent(context,FansMainActivity.class).putExtra(Constant.USERID, userId);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_main,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        intent = getIntent();
        userId = intent.getStringExtra(Constant.USERID);

        //粉丝主页
        mEntityPersonalHomeHttpModel = new HttpModel<>(EntityPersonalHome.class);
        mEntityStarsHttpModel = new HttpModel<>(EntityPageData.class);
        mEntityConversationHttpModel = new HttpModel<>(EntityPageData.class);
        personalHomeQuery();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        ivBack = findView(R.id.iv_left);
        ivBack = findView(R.id.iv_left);
        ivMore = findView(R.id.iv_right);

        llHead = findView(R.id.ll_head);
        //主页背景
        backImageView = findView(R.id.iv_backgrund_img);
        scrollView = findView(R.id.scrollview);
        initListeners();
        swipeRefresh = findView(R.id.swipe_refresh_widget);

        //头像
        ivPersonalImg = findView(R.id.personal_image);
        //昵称
        tvPersonalName = findView(R.id.personal_name);
        //签名
        tvPersonalSign = findView(R.id.personal_sign);
        //爱心
        tvLove = findView(R.id.tv_love);
        //贡献榜
        tvNo = findView(R.id.tv_No);
        //等级
        ivLvImg = findView(R.id.iv_lv_img);

        //关注
        tvFocus = findView(R.id.tv_focus);
        //已关注
        tvCancelFocued = findView(R.id.tv_cancel_focus);
        //互相关注
        /*tvCancelMutual = findView(R.id.tv_cancel_mutual);*/
        //私信
        tvTalk = findView(R.id.tv_talk);
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
                        personalHomeQuery();

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
        //头像
        String headUrl = member.getPhoto();
        if (headUrl != null) {
            Glide.with(context)
                    .load(headUrl)
                    .into(ivPersonalImg);
        } else {
            int defaultHead = R.mipmap.head;
            Glide.with(context)
                    .load(defaultHead)
                    .into(ivPersonalImg);
        }
        //个人主页
        String backUrl = member.getBackgruoud();
        if (backUrl != null) {
            Glide.with(context)
                    .load(backUrl)
                    .into(backImageView);
        } else {
            int defaultBackground = R.mipmap.personal_background;
            Glide.with(context)
                    .load(defaultBackground)
                    .into(backImageView);
        }
        //昵称
        tvPersonalName.setText(member.getNickname());
        //签名
        tvPersonalSign.setText(member.getAutograph());
        //爱心
        tvLove.setText("" + member.getLoveCount());
        //贡献榜
        tvNo.setText("" + rank);
        if (isFollow) {
            tvFocus.setVisibility(View.GONE);
            tvCancelFocued.setVisibility(View.VISIBLE);
        } else {
            tvFocus.setVisibility(View.VISIBLE);
            tvCancelFocued.setVisibility(View.GONE);
        }

        //等级
        level = member.getLevel();
        levelPic(level);
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
                            ivMore.setImageResource(R.mipmap.more);
                        } else if (y > 0 && y <= imageHeight) {
                            llHead.setBackgroundColor(Color.parseColor("#ddffffff"));
                            ivBack.setImageResource(R.mipmap.back_a);
                            ivMore.setImageResource(R.mipmap.gengduo);
                        }
                    }
                });

            }
        });


    }

    public void onResume() {

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        UMShareAPI.get(this).release();

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
    public void initEvent() {

        ivBack.setOnClickListener(this);
        ivMore.setOnClickListener(this);

        tvFocus.setOnClickListener(this);
        tvCancelFocued.setOnClickListener(this);
        /*tvCancelMutual.setOnClickListener(this);*/
        tvTalk.setOnClickListener(this);
    }

    public void share() {
        final String url ="http://mobile.umeng.com/social";
        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(FansMainActivity.this).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
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
                            Toast.makeText(FansMainActivity.this, "复制链接成功", Toast.LENGTH_LONG).show();

                        } else {
                            UMWeb web = new UMWeb(url);
                            web.setTitle("来自分享面板标题");
                            web.setDescription("来自分享面板内容");
                            web.setThumb(new UMImage(FansMainActivity.this, R.mipmap.syp_icon));
                            new ShareAction(FansMainActivity.this).withMedia(web)
                                    .setPlatform(share_media)
                                    .setCallback(mShareListener)
                                    .share();
                        }
                    }
                });
    }

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<FansMainActivity> mActivity;

        private CustomShareListener(FansMainActivity activity) {
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
            case R.id.tv_focus:
                focusFans(userId,tvFocus,tvCancelFocued);
                break;
            case R.id.tv_cancel_focus:
                cancelFans(userId,tvFocus,tvCancelFocued);
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

    //其他信息
    private void fragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        //爱豆
        FansMainIdolFragment fansMainIdolFragment = new FansMainIdolFragment();
        if (starsList.size() > 0) {
            Bundle bundleStars = new Bundle();
            bundleStars.putSerializable(Constant.STARSLIST, (Serializable) starsList);
            fansMainIdolFragment.setArguments(bundleStars);
            transaction.replace(R.id.list_focus_stars,fansMainIdolFragment);
        }
        //聊天室
        FansMainConversationFragment fansMainConversationFragment = new FansMainConversationFragment();
        if (chatRoomsList.size() > 0) {
            Bundle bundleConversation = new Bundle();
            bundleConversation.putSerializable(Constant.CONVERSATIONLIST, (Serializable) chatRoomsList);
            fansMainConversationFragment.setArguments(bundleConversation);
            transaction.replace(R.id.list_focus_conversation,fansMainConversationFragment);
        }
        //活动
        FansMainCampaignFragment fansMainActivityFragment = new FansMainCampaignFragment();
        if (activitiyList.size() > 0) {
            Bundle bundleActivity = new Bundle();
            bundleActivity.putSerializable(Constant.ACTIVITYLIST, (Serializable) activitiyList);
            fansMainActivityFragment.setArguments(bundleActivity);
            transaction.replace(R.id.list_focus_campaign,fansMainActivityFragment);
        }

        transaction.commit();
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

    public void personalHomeQuery() {
        setProgressBar();
        progressBar.show();

        //个人主页列表
        mEntityPersonalHomeHttpModel.get(userId, URL_BASE + URLConstant.PERSONALHOMEPAGE,1,this);

        String starRequest = HttpRequest.postFansFocusStarsQuery(userId);
        //我关注的明星列表
        mEntityStarsHttpModel.post(starRequest,URL_BASE + URLConstant.FOCUEDSTARS, 2, this);

        String ownerId = userId;
        //聊天室列表
        String request = HttpRequest.postConversationQueryList(ownerId);
        mEntityConversationHttpModel.post(request, URL_BASE + URLConstant.CONVERSATIONQUERY,3,this);
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
                EntityPersonalHome.Content personalHome = mEntityPersonalHomeHttpModel.getData().getContent();
                member = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(personalHome.getMember())
                        ,new TypeToken<Member>(){}.getType());
                /*starsList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(personalHome.getStars())
                        ,new TypeToken<List<FansIdol>>(){}.getType());*/
                /*chatRoomsList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(personalHome.getChatRooms())
                        ,new TypeToken<List<ChatingRoom>>(){}.getType());*/
                activitiyList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(personalHome.getActivitys())
                        ,new TypeToken<List<Activitys>>(){}.getType());
                rank = personalHome.getRank();
                isFollow = member.isFollow();

                initData();
                break;
            case 2:
                EntityPageData.Content entityStars = mEntityStarsHttpModel.getData().getContent();
                starsList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityStars.getData())
                        ,new TypeToken<List<FansIdol>>(){}.getType());
                break;
            case 3:
                EntityPageData.Content entityConversation = mEntityConversationHttpModel.getData().getContent();
                chatRoomsList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityConversation.getData())
                        ,new TypeToken<List<ChatingRoom>>(){}.getType());

                fragment();
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }

}
