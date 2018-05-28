package com.qingye.wtsyou.activity.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.basemodel.IdName;
import com.qingye.wtsyou.basemodel.POI;
import com.qingye.wtsyou.fragment.campaign.DetailedConversationFragment;
import com.qingye.wtsyou.model.EntityPaymentConfig;
import com.qingye.wtsyou.model.EntitySupportDetailed;
import com.qingye.wtsyou.model.PriceList;
import com.qingye.wtsyou.model.PriceListItem;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.DateUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;

import pub.devrel.easypermissions.EasyPermissions;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.util.HttpModel;
import zuo.biao.library.widget.CustomDialog;

import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.widget.AutoLineFeedLayout;
import com.qingye.wtsyou.widget.ObservableScrollView;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

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
    //发起人名字
    private TextView tvCreatorName;

    //进行中
    private LinearLayout llIng;
    private TextView tvTimeDown;
    //已结束
    private LinearLayout llEnd;
    private Button btnSupport;
    private Button btnSupportEnd;
    private LinearLayout llMore;

    private ProgressBar joinProgressBar;//参与进度条
    private RelativeLayout tvProgress;
    private TextView tvProgressValue;
    private ImageView ivBubble;
    private int textwidth,texthight;

    private LinearLayout supportOrder;
    private Button btnGone;

    private LinearLayout llHead;
    private ObservableScrollView scrollView;
    private ImageView backImageView;
    private int imageHeight;
    private View line;

    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    //应援价钱
    private EditText edtNumber;
    private BigDecimal number = BigDecimal.ONE;
    private TextView tvTotal;
    private double total;
    private BigDecimal price;

    //价格
    private EntitySupportDetailed entitySupportDetailed;
    private List<PriceListItem> priceListItem =  new ArrayList<>();
    private PriceList currentPriceList;
    private AutoLineFeedLayout layout;
    //付款按钮
    private Button btnPay;
    //支付方式
    private RadioGroup radioGroup;
    private RadioButton rbtWei,rbtAli;

    private HttpModel<EntityBase> mEntityBaseHttpModel;

    //类型
    private String type;

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

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastAction.ACTION_SUPPORT_REFRESH)) {
                refresh();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_detail,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        setProgressBar();
        progressBar.show();

        intent = getIntent();
        entitySupportDetailed = (EntitySupportDetailed) intent.getSerializableExtra(Constant.SUPPORTDETAILED);

        //参与应援
        mEntityBaseHttpModel = new HttpModel<>(EntityBase.class);

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BroadcastAction.ACTION_SUPPORT_REFRESH);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    public void getPriceListItem() {
        priceListItem.clear();
        List<PriceList> priceLists = entitySupportDetailed.getContent().getPriceList();
        for (PriceList priceList : priceLists) {
            PriceListItem priceItem = new PriceListItem();
            priceItem.setPriceList(priceList);
            priceItem.setSelector(false);
            priceListItem.add(priceItem);
        }
        //初始第一个未选择的价格
        price = priceListItem.get(0).getPriceList().getPrice();
        priceListItem.get(0).setSelector(true);
        currentPriceList = priceListItem.get(0).getPriceList();
        getTotalValue(number);
        /*total = number.multiply(price).doubleValue() + fare;
        tvTotal.setText(Double.toString(total));*/
        selectPrice();
    }

    public void selectPrice() {
        View view;
        final List<Button> buttonList = new ArrayList<>();
        for (int i = 0; i < priceListItem.size(); i ++) {
            view = LayoutInflater.from(SupportDetailedActivity.this).inflate(R.layout.list_radiobutton, null);
            final Button button = view.findViewById(R.id.btn);
            button.setTag(i);
            BigDecimal allBig = priceListItem.get(i).getPriceList().getPrice();
            double allDou = allBig.doubleValue();
            button.setText("￥" + Double.toString(allDou));
            boolean isSelect = priceListItem.get(i).getSelector();
            if (isSelect) {
                button.setSelected(true);
            }
            buttonList.add(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0;j < priceListItem.size();j ++) {
                        if (j == (int) button.getTag()) {
                            //设置选择的按钮为当前的价格
                            buttonList.get(j).setSelected(true);
                            priceListItem.get(j).setSelector(true);
                            currentPriceList = priceListItem.get(j).getPriceList();
                            price = priceListItem.get(j).getPriceList().getPrice();

                            getTotalValue(new BigDecimal(edtNumber.getText().toString()));
                        } else {
                            buttonList.get(j).setSelected(false);
                            priceListItem.get(j).setSelector(false);
                        }
                    }
                }
            });

            layout.addView(view, i);
        }

    }

    public void getTotalValue(BigDecimal number) {
        total = number.multiply(price).doubleValue();
        //tvTotal.setText(Double.toString(total));
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
        llMore = findViewById(R.id.ll_detailed_more);

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

        //发起人名字
        tvCreatorName = findViewById(R.id.tv_creator_name);
        //应援订单
        supportOrder = findViewById(R.id.support_order);
        //隐藏订单
        btnGone = findViewById(R.id.gone);
        //数量
        edtNumber = findViewById(R.id.edt_number_count);
        if (edtNumber.getTag() instanceof TextWatcher) {
            edtNumber.removeTextChangedListener((TextWatcher) edtNumber.getTag());
        }
        edtNumber.setText(String.valueOf(number));//变化监听
        TextWatcher watcher = new TextWatcher() {
            private CharSequence temp ;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s ;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    if (s.toString().equals("0")) {
                    } else {
                        getTotalValue(number = new BigDecimal(s.toString()));
                    }
                } else {

                }
            }
        };
        edtNumber.addTextChangedListener(watcher);
        edtNumber.setTag(watcher);
        //价格列表
        layout = findViewById(R.id.al);
        //付款按钮
        btnPay = findViewById(R.id.btn_pay);

        radioGroup = findViewById(R.id.radio_group);
        rbtWei = findViewById(R.id.rbtn_wei);
        rbtAli = findViewById(R.id.rbtn_ali);
        radioGroup.setOnCheckedChangeListener(new WayGrouplistener());
    }

    //radiobutton按钮监听
    class WayGrouplistener implements RadioGroup.OnCheckedChangeListener {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == rbtWei.getId()) {
                rbtWei.setChecked(true);
            }
            if (checkedId == rbtAli.getId()) {
                rbtAli.setChecked(true);
            }
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
        String url = entitySupportDetailed.getContent().getActivityPic();
        Glide.with(context)
                .load(url)
                .into(ivBackground);
        //名称
        tvName.setText(entitySupportDetailed.getContent().getActivityName());
        //项目详情
        tvDescription.setText(entitySupportDetailed.getContent().getDescription());
        type = entitySupportDetailed.getContent().getActivityTypeName();
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
                BigDecimal allBig = entitySupportDetailed.getContent().getSettingGoalsPrice();
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

            getPriceListItem();

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

            //发起人姓名
            tvCreatorName.setText(entitySupportDetailed.getContent().getCreateUserName());
        }

        progressBarDismiss();
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
        btnGone.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        llMore.setOnClickListener(this);
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
                if (type.equals("集资")) {
                    supportOrder.setVisibility(View.VISIBLE);
                } else {
                    support();
                }
                break;
            case R.id.gone:
                supportOrder.setVisibility(View.GONE);
                break;
            case R.id.btn_pay:
                if (rbtWei.isChecked()) {
                    showShortToast("此功能还在开发中");
                }
                else if (rbtAli.isChecked()) {
                    IdName idName = new IdName();
                    idName.setId("alipay_app");
                    idName.setName("支付宝支付");
                    //检查网络
                    if (NetUtil.checkNetwork(context)) {
                        pay(idName);
                    } else {
                        showShortToast(R.string.checkNetwork);
                    }
                }
                break;
            case R.id.ll_detailed_more:
                toActivity(DetailedActivity.createIntent(context, "应援详情",
                        entitySupportDetailed.getContent().getDetail()));
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

    private void pay(final IdName channel) {
        String[] perms = {android.Manifest.permission.READ_PHONE_STATE,};
        String uuid = entitySupportDetailed.getContent().getActivityId();
        BigDecimal carrieFree = BigDecimal.ZERO;
        POI poi = null;
        /*poi = defaultAddress.getPoi();
		LngLat lanLat = new LngLat();
		lanLat.setLng(new BigDecimal(118.109204));
		lanLat.setLat(new BigDecimal(24.490974));
        poi.setLngLat(lanLat);
		PCDT pcdt = new PCDT();
		pcdt.setProvince("福建省");
		pcdt.setProvinceCode("350000");
		pcdt.setCity("厦门市");
		pcdt.setCityCode("0592");
		pcdt.setDistrict("思明区");
		pcdt.setDistrictCode("350203");
		pcdt.setTownship("筼筜街道");
        poi.setPcdt(pcdt);*/
        String phone = "";
        String receiver = "";
        String SkuId = currentPriceList.getUuid();
        if (EasyPermissions.hasPermissions(this, perms)) {

            //检查网络
            if (NetUtil.checkNetwork(context)) {
                setProgressBar();
                progressBar.show();

                HttpRequest.postGetPaymentConfigOrder(0, uuid, carrieFree, price ,number,
                        channel, phone, poi, receiver, SkuId, new OnHttpResponseListener() {

                            @Override
                            public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                                if(!StringUtil.isEmpty(resultJson)){
                                    EntityPaymentConfig entityPaymentConfig = JSON.parseObject(resultJson,EntityPaymentConfig.class);
                                    if(entityPaymentConfig.isSuccess()) {
                                        aliPay(entityPaymentConfig);

                                        progressBarDismiss();
                                    } else {
                                        if (entityPaymentConfig.getCode().equals("401")) {
                                            showShortToast(R.string.tokenInvalid);
                                            toActivity(MainActivity.createIntent(context));
                                        } else {
                                            showShortToast(entityPaymentConfig.getMessage());
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

        } else {
            EasyPermissions.requestPermissions(this, "支付需要手机。。。。权限",
                    0, perms);
        }
    }

    public static final int SDK_PAY_FLAG = 100;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            //showShortToast("支付回调=>>>" + msg);
            switch (msg.what) {
                case SDK_PAY_FLAG: {

                    break;
                }
                default:
                    break;
            }
        }
    };

    private void aliPay(EntityPaymentConfig object) {
        final EntityPaymentConfig entity = object;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                try {
                    showShortToast("获取支付参数成功，开始调用支付");
                    final String payInfo = entity.getContent().getParameters().get(0).getValue();
                    if (payInfo == null) {
                        showShortToast("未调用支付，支付参数为空");
                        return;
                    }
                    PayTask alipay = new PayTask(SupportDetailedActivity.this);
                    Map<String, String> result = alipay.payV2(payInfo, true);

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void support() {
        setProgressBar();
        progressBar.show();

        mEntityBaseHttpModel.get(entitySupportDetailed.getContent().getActivityId(),URL_BASE + URLConstant.SUPPORT,2,this);
    }

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public void Success(String url, int RequestCode, EntityBase entityBase) {
        super.Success(url, RequestCode, entityBase);
        switch (RequestCode) {
            case 2:
                setProgressBar();
                progressBar.show();
                showShortToast(R.string.supportSuccess);

                Intent intent = new Intent(
                        BroadcastAction.ACTION_SUPPORT_REFRESH);
                // 发送广播
                sendBroadcast(intent);
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }
}
