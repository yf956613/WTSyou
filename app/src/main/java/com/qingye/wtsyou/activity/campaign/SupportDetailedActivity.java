package com.qingye.wtsyou.activity.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.fragment.campaign.DetailedConversationFragment;
import com.qingye.wtsyou.modle.EntitySupportDetailed;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.DateUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.widget.CustomDialog;
import com.qingye.wtsyou.widget.ObservableScrollView;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

public class SupportDetailedActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivShare;
    private TextView tvHead;

    //图片
    private ImageView ivBackground;
    //名称
    private TextView tvName;
    //项目详情
    private TextView tvDescription;
    //集资
    private LinearLayout llRaise;
    //其他
    private LinearLayout llOther;
    //已筹集
    private TextView tvCrowdPrice;
    //目标金额
    private TextView tvCrowdGoalPrice;
    //已参与
    private TextView tvCrowdNum;
    //目标人数
    private TextView tvCrowdGoalNum;

    //进行中
    private LinearLayout llIng;
    private TextView tvTimeDown;
    //已结束
    private LinearLayout llEnd;
    private Button btnSupport;
    private Button btnSupportEnd;
    private ProgressBar joinProgressBar;//参与进度条
    private RelativeLayout tvProgress;
    private TextView tvProgressValue;
    private ImageView ivBubble;
    private int textwidth,texthight;

    private LinearLayout llHead;
    private ObservableScrollView scrollView;
    private ImageView backImageView;
    private int imageHeight;
    private View line;

    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    private EntitySupportDetailed entitySupportDetailed;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, EntitySupportDetailed entitySupportDetailed) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.SUPPORTDETAILED, entitySupportDetailed);//放进数据流中
        return new Intent(context, SupportDetailedActivity.class).putExtras(bundle);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_detail,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        setProgressBar();
        progressBar.show();

        intent = getIntent();
        entitySupportDetailed = (EntitySupportDetailed) intent.getSerializableExtra(Constant.SUPPORTDETAILED);

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
        ivBack.setImageResource(R.mipmap.back_a);
        ivShare = findViewById(R.id.iv_right);
        ivShare.setImageResource(R.mipmap.share_g);
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("应援详情");

        //图片
        ivBackground = findViewById(R.id.iv_campaign_background_img);
        //名称
        tvName = findViewById(R.id.tv_campaign_name);
        //集资
        llRaise = findViewById(R.id.ll_raise);
        //其他
        llOther = findViewById(R.id.ll_other);
        //项目详情
        tvDescription = findViewById(R.id.tv_suport_content);
        //已筹集
        tvCrowdPrice = findViewById(R.id.tv_crowded_value);
        //目标金额
        tvCrowdGoalPrice = findViewById(R.id.tv_target_price_value);
        //参与人数
        tvCrowdNum = findViewById(R.id.tv_join_value);
        //目标人数
        tvCrowdGoalNum = findViewById(R.id.tv_target_value);

        //进行中
        llIng = findViewById(R.id.ll_ing);
        tvTimeDown = findViewById(R.id.tv_campaign_end_time);
        //已结束
        llEnd = findViewById(R.id.ll_end);

        btnSupport = findViewById(R.id.btn_support);
        btnSupportEnd = findViewById(R.id.btn_support_end);

        //进度条
        joinProgressBar = findViewById(R.id.join_progressbar);
        //气泡
        tvProgressValue = findViewById(R.id.text_value);
        ivBubble = findViewById(R.id.iv_bubble);
        tvProgress = findViewById(R.id.progress_value);

        llHead = findViewById(R.id.ll_head);
        backImageView = findViewById(R.id.iv_campaign_background_img);
        scrollView = findViewById(R.id.scrollview);
        line = findViewById(R.id.line);
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
                            line.setVisibility(View.GONE);
                        } else if (y > 0 && y <= imageHeight) {
                            line.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
    }

    @SuppressLint("NewApi")
    public static void setProgressDrawable(@NonNull ProgressBar bar, @DrawableRes int resId) {
        Drawable layerDrawable = bar.getResources().getDrawable(resId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable d = getMethod("tileify", bar, new Object[] { layerDrawable, false });
            bar.setProgressDrawable(d);
        } else {
            bar.setProgressDrawableTiled(layerDrawable);
        }
    }

    private static Drawable getMethod(String methodName, Object o, Object[] paras) {
        Drawable newDrawable = null;
        try {
            Class c[] = new Class[2];
            c[0] = Drawable.class;
            c[1] = boolean.class;
            Method method = ProgressBar.class.getDeclaredMethod(methodName, c);
            method.setAccessible(true);
            newDrawable = (Drawable) method.invoke(o, paras);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return newDrawable;
    }

    private void setProgressText(final int progressValue) {
        //气泡提示
        tvProgress.post(new Runnable() {
            @Override
            public void run() {
                tvProgress.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                textwidth = tvProgress.getMeasuredWidth();
                texthight = tvProgress.getMeasuredHeight();
            }
        });

        //进度条
        joinProgressBar.post(new Runnable() {
            @Override
            public void run() {
                int width = joinProgressBar.getWidth();
                int height = joinProgressBar.getHeight();
                int[] location = new int[2];
                joinProgressBar.getLocationOnScreen(location);
                //获得进度条在屏幕中的x，y值
                int x = location[0];
                int y = location[1];
                //目前textview应该在的x值
                int textlocation = width * progressValue / 100 + x;
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvProgress.getLayoutParams();
                params.leftMargin = ( textlocation - textwidth / 2 );// 通过自定义坐标来放置你的控件
                tvProgress.setLayoutParams(params);
            }
        });

        tvProgressValue.setText(Integer.toString(progressValue));
    }

    @Override
    public void initData() {
        //图片
        String url = entitySupportDetailed.getContent().getActivityIcon();
        Glide.with(context)
                .load(url)
                .into(ivBackground);
        //名称
        tvName.setText(entitySupportDetailed.getContent().getActivityName());
        //项目详情
        tvDescription.setText(entitySupportDetailed.getContent().getDescription());
        String type = entitySupportDetailed.getContent().getActivityTypeName();
        //显示已参与、目标
        if (type.equals("集资")) {
            llRaise.setVisibility(View.VISIBLE);
            llOther.setVisibility(View.GONE);

            if (entitySupportDetailed.getContent().getSettingGoalsPrice() != null) {
                //筹集金额
                BigDecimal joinBig = entitySupportDetailed.getContent().getSupportPrice();
                double joinDou = joinBig.doubleValue();
                int joinInt = (int) joinDou;
                tvCrowdPrice.setText(Double.toString(joinDou));
                //目标金额
                BigDecimal allBig = entitySupportDetailed.getContent().getSettingGoalsNum();
                double allDou = allBig.doubleValue();
                int allInt = (int) allDou;
                tvCrowdGoalPrice.setText(Double.toString(allDou));
                int progressValueInt = 0;
                if (allDou > 0) {
                    //进度条
                    BigDecimal progressValueBig = new BigDecimal(joinDou/allDou);
                    String progressValueStr = StringUtil.getPrice(progressValueBig);
                    progressValueInt = StringUtil.stringToInt(progressValueStr);
                }
                joinProgressBar.setProgress(progressValueInt);

                //气泡位置
                setProgressText(progressValueInt);
            }

        } else {
            llRaise.setVisibility(View.GONE);
            llOther.setVisibility(View.VISIBLE);

            if (entitySupportDetailed.getContent().getSettingGoalsNum() != null) {
                //参与人数
                BigDecimal joinBig = entitySupportDetailed.getContent().getSupportNum();
                double joinDou = joinBig.doubleValue();
                int joinInt = (int) joinDou;
                tvCrowdNum.setText(Integer.toString(joinInt));
                //目标人数
                BigDecimal allBig = entitySupportDetailed.getContent().getSettingGoalsNum();
                double allDou = allBig.doubleValue();
                int allInt = (int) allDou;
                tvCrowdGoalNum.setText(Integer.toString(allInt));

                int progressValueInt = 0;
                if (allDou > 0) {
                    //进度条
                    BigDecimal progressValueBig = new BigDecimal(joinDou/allDou);
                    String progressValueStr = StringUtil.getPrice(progressValueBig);
                    progressValueInt = StringUtil.stringToInt(progressValueStr);
                }
                joinProgressBar.setProgress(progressValueInt);

                //气泡位置
                setProgressText(progressValueInt);
            }

        }
        //判断是否已经结束，显示不一样的样式
        String state = entitySupportDetailed.getContent().getState();
        if (state.equals("supporting")) {
            //进度条
            setProgressDrawable(joinProgressBar, R.drawable.progress_horizontal2);
            //气泡
            ivBubble.setImageResource(R.mipmap.combined_shape);

            //按钮
            btnSupport.setVisibility(View.VISIBLE);
            btnSupportEnd.setVisibility(View.GONE);

            //倒计时
            llIng.setVisibility(View.VISIBLE);
            //获取当前时间
            long currentTime = System.currentTimeMillis();
            if (entitySupportDetailed.getContent().getDeadline() != null) {
                long endTime = DateUtil.dateToLong(entitySupportDetailed.getContent().getDeadline());
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
            llEnd.setVisibility(View.GONE);
        } else {
            //进度条
            setProgressDrawable(joinProgressBar, R.drawable.progress_horizontal3);
            //气泡
            ivBubble.setImageResource(R.mipmap.qipaohui);

            //按钮
            btnSupport.setVisibility(View.GONE);
            btnSupportEnd.setVisibility(View.VISIBLE);

            //倒计时
            llIng.setVisibility(View.VISIBLE);
            llEnd.setVisibility(View.GONE);
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
        btnSupport.setOnClickListener(this);
    }

    public void refresh() {
        String uuid = entitySupportDetailed.getContent().getActivityId();

        if (NetUtil.checkNetwork(this)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.getSupportDetailed(0, uuid, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntitySupportDetailed entitySupportDetailed =  JSON.parseObject(resultJson,EntitySupportDetailed.class);
                        if(entitySupportDetailed.isSuccess()){
                            //成功
                            //showShortToast(R.string.getSuccess);

                            progressBarDismiss();
                        }else{//显示失败信息
                            if (entitySupportDetailed.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entitySupportDetailed.getMessage());
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

        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                break;
            case R.id.btn_support:
                showShortToast("应援成功！请耐心等待结果~");
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
