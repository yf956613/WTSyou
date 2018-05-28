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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import zuo.biao.library.model.EntityBase;
import com.qingye.wtsyou.fragment.campaign.DetailedConversationFragment;
import com.qingye.wtsyou.model.EntityVoteDetailed;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.DateUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.widget.CircleProgress;
import zuo.biao.library.widget.CustomDialog;
import com.qingye.wtsyou.widget.ObservableScrollView;

import java.math.BigDecimal;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_1;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_2;

public class VoteDetailedActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivShare;
    //活动
    private ImageView ivBackground;
    private ImageView ivImg;
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

    private Button btnVote,btnVoteEnd;
    private LinearLayout llMore;

    private CircleProgress mcircleProgressBar;

    private LinearLayout llHead;
    private ObservableScrollView scrollView;
    private ImageView backImageView;
    private int imageHeight;

    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    private EntityVoteDetailed entityVoteDetailed;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, EntityVoteDetailed entityVoteDetailed) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.VOTEDETAILED, entityVoteDetailed);//放进数据流中
        return new Intent(context, VoteDetailedActivity.class).putExtras(bundle);
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
            if (action.equals(BroadcastAction.ACTION_ADDRESS_REFRESH)) {
                refresh();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_detail,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);


        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BroadcastAction.ACTION_VOTE_REFRESH);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);

        setProgressBar();
        progressBar.show();

        intent = getIntent();
        entityVoteDetailed = (EntityVoteDetailed) intent.getSerializableExtra(Constant.VOTEDETAILED);

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
        //参与人数
        tvJoin = findViewById(R.id.tv_campaign_value);
        //目标人数
        tvAll = findViewById(R.id.tv_campaign_all_value);
        //标签
        tvTag = findViewById(R.id.tv_tag);
        //地址
        tvAddress = findViewById(R.id.tv_campaign_place);
        //活动时间
        tvTime = findViewById(R.id.tv_campaign_time);
        //截止时间
        tvDeadline = findViewById(R.id.tv_campaign_end_time);
        //项目简介
        tvDescription = findViewById(R.id.tv_crowd_content);

        //发起人
        tvCreatorName = findViewById(R.id.tv_creator_name);

        btnVote = findViewById(R.id.btn_vote);
        btnVoteEnd = findViewById(R.id.btn_vote_end);
        llMore = findViewById(R.id.ll_detailed_more);

        mcircleProgressBar = findViewById(R.id.join_progressbar);
        mcircleProgressBar.setPercentColor(R.color.black_text1);

        llHead = findViewById(R.id.ll_head);
        backImageView = findViewById(R.id.iv_campaign_background_img);
        scrollView = findViewById(R.id.scrollview);
        initListeners();

        DetailedConversationFragment campaignDetailedConversationFragment = new DetailedConversationFragment();
        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_conversation,campaignDetailedConversationFragment);
        transaction.commit();
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
        String urlBacground = entityVoteDetailed.getContent().getActivityPic();
        Glide.with(context)
                .load(urlBacground)
                .apply(bitmapTransform(new BlurTransformation(25)))
                .into(ivBackground);
        //小图
        String urlImg = entityVoteDetailed.getContent().getActivityIcon();
        Glide.with(context)
                .load(urlImg)
                .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(ivImg);
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

        //发起人
        tvCreatorName.setText(entityVoteDetailed.getContent().getCreateUserName());

        progressBarDismiss();
    }

    public void onResume() {
        //refresh();
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
        btnVote.setOnClickListener(this);
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
            case R.id.btn_vote:
                vote();
                break;
            case R.id.ll_detailed_more:
                toActivity(DetailedActivity.createIntent(context, "项目详情",
                        entityVoteDetailed.getContent().getDetail()));
                break;
            default:
                break;
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

                            Intent intent = new Intent(
                                    BroadcastAction.ACTION_VOTE_REFRESH);
                            // 发送广播
                            sendBroadcast(intent);

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

    public void refresh() {
        String uuid = entityVoteDetailed.getContent().getActivityId();

        //检查网络
        if (NetUtil.checkNetwork(this)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.getVoteDetailed(0, uuid, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntityVoteDetailed entityVoteDetailed =  JSON.parseObject(resultJson,EntityVoteDetailed.class);
                        if(entityVoteDetailed.isSuccess()){
                            //成功
                            //showShortToast(R.string.getSuccess);

                            progressBarDismiss();
                        }else{//显示失败信息
                            if (entityVoteDetailed.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entityVoteDetailed.getMessage());
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
}
