package com.qingye.wtsyou.activity.campaign;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.fragment.campaign.DetailedConversationFragment;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.ChatingRoom;
import com.qingye.wtsyou.model.EntitySaleDetailed;
import com.qingye.wtsyou.model.PriceList;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.DateUtil;
import com.qingye.wtsyou.utils.URLConstant;
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
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.widget.CustomDialog;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_1;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_2;
import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class SaleDetailedActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivShare;
    //活动
    private ImageView ivBackground;
    private ImageView ivImg;
    private TextView tvName;
    private TextView tvTag;
    private TextView tvAddress;
    private TextView tvTime;
    private TextView tvDescription;
    private TextView tvMin;
    private TextView tvMax;

    private Button btnBuy,btnBuyEnd;
    private LinearLayout llMore;

    private LinearLayout llHead;
    private ObservableScrollView scrollView;
    private ImageView backImageView;
    private int imageHeight;

    private RelativeLayout llConversation;
    private LinearLayout llAssociationConversation;

    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    private String uuid;
    private EntitySaleDetailed entitySaleDetailed;
    private List<PriceList> priceLists = new ArrayList<>();
    private List<ChatingRoom> chatingRoomList = new ArrayList<>();

    private HttpModel<EntitySaleDetailed> mDetailedHttpModel;

    private UMShareListener mShareListener;
    private ShareAction mShareAction;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, String uuid) {
        return new Intent(context, SaleDetailedActivity.class).putExtra(Constant.UUID, uuid);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_detail,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        setProgressBar();
        progressBar.show();

        intent = getIntent();
        uuid = intent.getStringExtra(Constant.UUID);

        //获取详情
        mDetailedHttpModel = new HttpModel<>(EntitySaleDetailed.class);
        refresh();

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
        ivImg = findView(R.id.iv_campaign_img);
        //名称
        tvName = findView(R.id.tv_campaign_name);
        //标签
        tvTag = findView(R.id.tv_tag);
        //地址
        tvAddress = findView(R.id.tv_campaign_place);
        //时间
        tvTime = findView(R.id.tv_campaign_time);
        //简介
        tvDescription = findView(R.id.tv_sale_content);
        //最小
        tvMin = findView(R.id.tv_min_value);
        //最大
        tvMax = findView(R.id.tv_max_value);

        btnBuy = findView(R.id.btn_buy);
        btnBuyEnd = findView(R.id.btn_buy_end);
        llMore = findView(R.id.ll_detailed_more);

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
        String urlBacground = entitySaleDetailed.getContent().getActivityPic();
        Glide.with(context)
                .load(urlBacground)
                .apply(bitmapTransform(new BlurTransformation(25)))
                .into(ivBackground);
        //小图
        String urlImg = entitySaleDetailed.getContent().getActivityIcon();
        Glide.with(context)
                .load(urlImg)
                .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(ivImg);
        //活动名称
        tvName.setText(entitySaleDetailed.getContent().getActivityName());
        //标签
        String state = entitySaleDetailed.getContent().getState();
        if (state.equals("saleout")) {
            tvTag.setText("已售完");
            tvTag.setBackground(getResources().getDrawable(R.drawable.corners_gradient_gray));
            btnBuy.setVisibility(View.GONE);
            btnBuyEnd.setVisibility(View.VISIBLE);
        }
        else if (state.equals("saling")) {
            tvTag.setText("售票中");
            btnBuy.setVisibility(View.VISIBLE);
            btnBuyEnd.setVisibility(View.GONE);
        }
        //地址
        /*String province = entitySaleDetailed.getContent().getAddress().getPcdt().getProvince();
        String city = entitySaleDetailed.getContent().getAddress().getPcdt().getCity();
        String district = entitySaleDetailed.getContent().getAddress().getPcdt().getDistrict();
        String township = entitySaleDetailed.getContent().getAddress().getPcdt().getTownship();
        String address = entitySaleDetailed.getContent().getAddress().getAddress();*/
        tvAddress.setText(entitySaleDetailed.getContent().getStadiumsName());
        //时间
        tvTime.setText(DateUtil.resverDate(entitySaleDetailed.getContent().getStartTimeStr(),DATE_PATTERN_2,DATE_PATTERN_1));
        //项目简介
        tvDescription.setText(entitySaleDetailed.getContent().getDescription());

        //排序
        priceLists = entitySaleDetailed.getContent().getPriceList();
        double[] priceList = new double[priceLists.size()];
        for (int i = 0;i < priceLists.size();i ++) {
            priceList[i] = priceLists.get(i).getPrice();
        }
        sortInsert(priceList);
        //最小值
        if (priceLists.size() > 1) {
            tvMin.setText(Double.toString(priceList[0]) + " - ");
            tvMax.setText(Double.toString(priceList[priceList.length - 1]));
        } else {
            tvMin.setText(Double.toString(priceList[0]));
        }

        if (entitySaleDetailed.getContent().getChartRoomList() == null && !entitySaleDetailed.getContent().getChartRoomList().isEmpty()) {
            llConversation.setVisibility(View.GONE);
        } else {
            llConversation.setVisibility(View.VISIBLE);
            chatingRoomList = entitySaleDetailed.getContent().getChartRoomList();
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

    public double[] sortInsert(double[] array){
        for(int i = 1;i < array.length;i++){
            double temp = array[i];
            int j;
            for(j = i-1;j >= 0 && temp < array[j]; j--){
                array[j + 1] = array[j];
            }
            array[j + 1] = temp;
        }
        return array;
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
        btnBuy.setOnClickListener(this);
        llMore.setOnClickListener(this);
    }

    public void share() {
        final String url ="http://mobile.umeng.com/social";
        mShareListener = new CustomShareListener(this);
        /*增加自定义按钮的分享面板*/
        mShareAction = new ShareAction(SaleDetailedActivity.this).setDisplayList(
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
                            Toast.makeText(SaleDetailedActivity.this, "复制链接成功", Toast.LENGTH_LONG).show();

                        } else {
                            UMWeb web = new UMWeb(url);
                            web.setTitle("来自分享面板标题");
                            web.setDescription("来自分享面板内容");
                            web.setThumb(new UMImage(SaleDetailedActivity.this, R.mipmap.syp_icon));
                            new ShareAction(SaleDetailedActivity.this).withMedia(web)
                                    .setPlatform(share_media)
                                    .setCallback(mShareListener)
                                    .share();
                        }
                    }
                });
    }

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<SaleDetailedActivity> mActivity;

        private CustomShareListener(SaleDetailedActivity activity) {
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
            case R.id.btn_buy:
                toActivity(SaleOrderActivity.createIntent(context, entitySaleDetailed));
                break;
            case R.id.ll_detailed_more:
                toActivity(DetailedActivity.createIntent(context, "项目详情",
                        entitySaleDetailed.getContent().getDetail()));
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

    public void refresh() {

        setProgressBar();
        progressBar.show();

        mDetailedHttpModel.get(uuid,URL_BASE + URLConstant.SALEDETIALED,1,this);
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
                entitySaleDetailed = mDetailedHttpModel.getData();
                initData();
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }
}
