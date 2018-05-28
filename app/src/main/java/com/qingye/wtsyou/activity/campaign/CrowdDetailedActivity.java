package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.model.EntityCrowdDetailed;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.DateUtil;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.widget.CircleProgress;
import zuo.biao.library.widget.CustomDialog;
import com.qingye.wtsyou.widget.ObservableScrollView;

import java.math.BigDecimal;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.util.HttpModel;
import zuo.biao.library.util.StringUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_1;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_2;
import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class CrowdDetailedActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivShare;
    //活动
    private ImageView ivBackground;
    private ImageView ivImg;
    private TextView tvName;
    private TextView tvCrowdValue;
    private TextView tvTag;
    private TextView tvTime;
    private TextView tvAddress;
    private TextView tvCrowdNum;
    private TextView tvChatroomNum;
    private TextView tvTarget;
    private TextView tvDescription;
    //倒计时
    private TextView tvTimeDown;

    private Button btnCrowd,btnCrowdEnd;
    private LinearLayout llParticipate;
    private LinearLayout llConversation;
    private LinearLayout llDetailed;
    private LinearLayout llMore;

    private CircleProgress mcircleProgressBar;

    private LinearLayout llHead;
    private ObservableScrollView scrollView;
    private ImageView backImageView;
    private int imageHeight;

    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    private EntityCrowdDetailed entityCrowdDetailed;

    private HttpModel<EntityCrowdDetailed> mEntityCrowdDetailedHttpModel;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, EntityCrowdDetailed entityCrowdDetailed) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.CROWDDETAILED, entityCrowdDetailed);//放进数据流中
        return new Intent(context, CrowdDetailedActivity.class).putExtras(bundle);
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
            if (action.equals(BroadcastAction.ACTION_CROWD_REFRESH)) {
                refresh();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowd_detailed,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        intent = getIntent();
        entityCrowdDetailed = (EntityCrowdDetailed) intent.getSerializableExtra(Constant.CROWDDETAILED);

        //获取详情
        mEntityCrowdDetailedHttpModel = new HttpModel<>(EntityCrowdDetailed.class);

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BroadcastAction.ACTION_CROWD_REFRESH);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);

        setProgressBar();
        progressBar.show();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        swipeRefresh = findViewById(R.id.swipe_refresh_widget);

        ivBack = findViewById(R.id.iv_left);
        ivShare = findViewById(R.id.iv_right);
        //活动
        //背景
        ivBackground = findViewById(R.id.iv_campaign_background_img);
        //小图
        ivImg = findViewById(R.id.iv_campaign_img);
        //活动名称
        tvName = findViewById(R.id.tv_campaign_name);
        //已筹集
        tvCrowdValue = findViewById(R.id.tv_campaign_value);
        //标签
        tvTag = findViewById(R.id.tv_tag);
        //时间
        tvTime = findViewById(R.id.tv_campaign_date);
        //地址
        tvAddress = findViewById(R.id.tv_campaign_address);
        //众筹人数
        tvCrowdNum = findViewById(R.id.tv_crowd_num);
        //聊天室
        tvChatroomNum = findViewById(R.id.tv_conversation_num);
        //筹资目标
        tvTarget = findViewById(R.id.tv_crowd_target);
        //简介
        tvDescription = findViewById(R.id.tv_crowd_content);
        //倒计时
        tvTimeDown = findViewById(R.id.tv_campaign_end_time);

        btnCrowd = findViewById(R.id.btn_crowd);
        btnCrowdEnd = findViewById(R.id.btn_crowd_end);

        llParticipate = findViewById(R.id.ll_participate);
        llConversation = findViewById(R.id.ll_conversation);
        llDetailed = findViewById(R.id.ll_detailed);
        llMore = findViewById(R.id.ll_detailed_more);

        mcircleProgressBar = findViewById(R.id.join_progressbar);
        mcircleProgressBar.setPercentColor(R.color.black_text1);

        llHead = findViewById(R.id.ll_head);
        backImageView = findViewById(R.id.iv_campaign_background_img);
        scrollView = findViewById(R.id.scrollview);
        initListeners();

        /*DetailedConversationFragment campaignDetailedConversationFragment = new DetailedConversationFragment();
        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_conversation,campaignDetailedConversationFragment);
        transaction.commit();*/
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
        String urlBackground = entityCrowdDetailed.getContent().getActivityPic();
        Glide.with(context)
                .load(urlBackground)
                .apply(bitmapTransform(new BlurTransformation(25)))
                .into(ivBackground);
        //小图
        String urlImg = entityCrowdDetailed.getContent().getActivityIcon();
        Glide.with(context)
                .load(urlImg)
                .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(ivImg);
        //活动名称
        tvName.setText(entityCrowdDetailed.getContent().getActivityName());
        //已筹集
        BigDecimal crowdBig = entityCrowdDetailed.getContent().getCrowdNum();
        double crowdDou = crowdBig.doubleValue();
        tvCrowdValue.setText(Double.toString(crowdDou));
        //标签
        String state = entityCrowdDetailed.getContent().getState();
        if (state.equals("crowdsuccess")) {
            tvTag.setText("众筹成功");
            tvTag.setBackground(getResources().getDrawable(R.drawable.corners_gradient_gray));
            btnCrowd.setVisibility(View.GONE);
            btnCrowdEnd.setVisibility(View.VISIBLE);
        }
        else if (state.equals("crowdfail") ) {
            tvTag.setText("众筹失败");
            tvTag.setBackground(getResources().getDrawable(R.drawable.corners_gradient_gray));
            btnCrowd.setVisibility(View.GONE);
            btnCrowdEnd.setVisibility(View.VISIBLE);
        }
        else if (state.equals("crowding")) {
            tvTag.setText("众筹中");
            tvTag.setBackground(getResources().getDrawable(R.drawable.corners_gradient_blue));
            btnCrowd.setVisibility(View.VISIBLE);
            btnCrowdEnd.setVisibility(View.GONE);
        }
        //时间
        tvTime.setText(DateUtil.resverDate(entityCrowdDetailed.getContent().getStartTimeStr(),DATE_PATTERN_2,DATE_PATTERN_1));
        //地址
        /*String province = entityCrowdDetailed.getContent().getAddress().getPcdt().getProvince();
        String city = entityCrowdDetailed.getContent().getAddress().getPcdt().getCity();
        String district = entityCrowdDetailed.getContent().getAddress().getPcdt().getDistrict();
        String township = entityCrowdDetailed.getContent().getAddress().getPcdt().getTownship();
        String address = entityCrowdDetailed.getContent().getAddress().getAddress();*/
        tvAddress.setText(entityCrowdDetailed.getContent().getAddress().getPoiName());
        //聊天室
        tvChatroomNum.setText(Integer.toString(entityCrowdDetailed.getContent().getChatroomNumber()));
        //众筹人数
        BigDecimal joinBigNum = entityCrowdDetailed.getContent().getCrowdNum();
        double joinDouNum = joinBigNum.doubleValue();
        int joinIntNum = (int) joinDouNum;
        tvCrowdNum.setText(Integer.toString(joinIntNum));
        //筹集到的金额
        BigDecimal joinBig = entityCrowdDetailed.getContent().getCrowdPrice();
        double joinDou = joinBig.doubleValue();
        tvCrowdValue.setText(Double.toString(joinDou));
        //筹资目标
        BigDecimal allBig = entityCrowdDetailed.getContent().getSettingGoalsPrice();
        double allDou = allBig.doubleValue();
        tvTarget.setText(Double.toString(allDou));

        int progressValueInt = 0;
        if (allDou > 0) {
            //进度条
            BigDecimal progressValueBig = new BigDecimal(joinDou/allDou);
            String progressValueStr = StringUtil.getPrice(progressValueBig);
            progressValueInt = StringUtil.stringToInt(progressValueStr);
        }

        mcircleProgressBar.setMaxProgress(100);
        mcircleProgressBar.updateProgress(progressValueInt,700);

        //项目简介
        tvDescription.setText(entityCrowdDetailed.getContent().getDescription());

        //获取当前时间
        long currentTime = System.currentTimeMillis();
        if (entityCrowdDetailed.getContent().getDeadline() != null) {
            long endTime = DateUtil.dateToLong(entityCrowdDetailed.getContent().getDeadline());
            //时间之差
            long betweenTime = DateUtil.calculateTimeDifference(endTime, currentTime);
            if (betweenTime > 0) {
                DateUtil.downTime( tvTimeDown, betweenTime, 1000, 0 + "天" + 0 + "小时" + 0 + "分钟"
                        + 0 + "秒");
            } else {
                tvTimeDown.setText(0 + "天" + 0 + "小时" + 0 + "分钟"
                        + 0 + "秒");
            }
        } else {
            tvTimeDown.setText(0 + "天" + 0 + "小时" + 0 + "分钟"
                    + 0 + "秒");
        }

        progressBarDismiss();

    }

    public void onResume() {
        refresh();
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
        // 设置子视图是否允许滚动到顶部
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
        btnCrowd.setOnClickListener(this);
        llParticipate.setOnClickListener(this);
        llConversation.setOnClickListener(this);
        llDetailed.setOnClickListener(this);
        llMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                break;
            case R.id.btn_crowd:
                toActivity(CrowdOrderActivity.createIntent(context, entityCrowdDetailed));
                break;
            case R.id.ll_participate:
                toActivity(CrowdFansActivity.createIntent(context, entityCrowdDetailed));
                break;
            case R.id.ll_conversation:
                toActivity(CrowdConversationActivity.createIntent(context));
                break;
            case R.id.ll_detailed:
                toActivity(CrowdMoneyDetailedActivity.createIntent(context, entityCrowdDetailed));
                break;
            case R.id.ll_detailed_more:
                toActivity(DetailedActivity.createIntent(context, "项目详情",
                        entityCrowdDetailed.getContent().getDetail()));
                break;
            default:
                break;
        }
    }

    public void refresh() {
        /*String uuid = entityCrowdDetailed.getContent().getActivityId();

        if (NetUtil.checkNetwork(this)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.getCrowdDetailed(0, uuid, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityCrowdDetailed entityCrowdDetailed =  JSON.parseObject(resultJson,EntityCrowdDetailed.class);
                        if(entityCrowdDetailed.isSuccess()){
                            //成功
                            //showShortToast(R.string.getSuccess);

                            progressBarDismiss();
                        }else{//显示失败信息
                            if (entityCrowdDetailed.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityCrowdDetailed.getMessage());
                            }
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
*/
        mEntityCrowdDetailedHttpModel.get(entityCrowdDetailed.getContent().getActivityId(),URL_BASE + URLConstant.CROWDDETIALED,2,this);
        initData();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onDragBottom(boolean rightToLeft) {
        finish();
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

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public void Success(String url, int RequestCode, EntityBase entityBase) {
        super.Success(url, RequestCode, entityBase);
        switch (RequestCode) {
            case 1:
                setProgressBar();
                progressBar.show();
                entityCrowdDetailed = mEntityCrowdDetailedHttpModel.getData();
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }
}
