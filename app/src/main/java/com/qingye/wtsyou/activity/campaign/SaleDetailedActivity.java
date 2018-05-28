package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.qingye.wtsyou.fragment.campaign.DetailedConversationFragment;
import com.qingye.wtsyou.model.EntitySaleDetailed;
import com.qingye.wtsyou.model.PriceList;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.DateUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import zuo.biao.library.widget.CustomDialog;
import com.qingye.wtsyou.widget.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;

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

    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    private EntitySaleDetailed entitySaleDetailed;
    private List<PriceList> priceLists = new ArrayList<>();

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, EntitySaleDetailed entitySaleDetailed) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.SALEDETAILED, entitySaleDetailed);//放进数据流中
        return new Intent(context, SaleDetailedActivity.class).putExtras(bundle);
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
        entitySaleDetailed = (EntitySaleDetailed) intent.getSerializableExtra(Constant.SALEDETAILED);

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
        //名称
        tvName = findViewById(R.id.tv_campaign_name);
        //标签
        tvTag = findViewById(R.id.tv_tag);
        //地址
        tvAddress = findViewById(R.id.tv_campaign_place);
        //时间
        tvTime = findViewById(R.id.tv_campaign_time);
        //简介
        tvDescription = findViewById(R.id.tv_sale_content);
        //最小
        tvMin = findViewById(R.id.tv_min_value);
        //最大
        tvMax = findViewById(R.id.tv_max_value);

        btnBuy = findViewById(R.id.btn_buy);
        btnBuyEnd = findViewById(R.id.btn_buy_end);
        llMore = findViewById(R.id.ll_detailed_more);

        DetailedConversationFragment campaignDetailedConversationFragment = new DetailedConversationFragment();
        //注意这里是调用getSupportFragmentManager()方法
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_conversation,campaignDetailedConversationFragment);
        transaction.commit();

        llHead = findViewById(R.id.ll_head);
        backImageView = findViewById(R.id.iv_campaign_background_img);
        scrollView = findViewById(R.id.scrollview);
        initListeners();
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
            tvTag.setBackground(getResources().getDrawable(R.drawable.corners_gradient_blue));
            btnBuy.setVisibility(View.VISIBLE);
            btnBuyEnd.setVisibility(View.GONE);
        }
        //地址
        /*String province = entitySaleDetailed.getContent().getAddress().getPcdt().getProvince();
        String city = entitySaleDetailed.getContent().getAddress().getPcdt().getCity();
        String district = entitySaleDetailed.getContent().getAddress().getPcdt().getDistrict();
        String township = entitySaleDetailed.getContent().getAddress().getPcdt().getTownship();
        String address = entitySaleDetailed.getContent().getAddress().getAddress();
        tvAddress.setText(entitySaleDetailed.getContent().getStadiumsName());*/
        //时间
        tvTime.setText(DateUtil.resverDate(entitySaleDetailed.getContent().getStartTimeStr(),DATE_PATTERN_2,DATE_PATTERN_1));
        //项目简介
        tvDescription.setText(entitySaleDetailed.getContent().getDescription());

        //排序
        priceLists = entitySaleDetailed.getContent().getPriceList();
        double[] priceList = new double[priceLists.size()];
        for (int i = 0;i < priceLists.size();i ++) {
            priceList[i] = priceLists.get(i).getPrice().doubleValue();
        }
        sortInsert(priceList);
        //最小值
        if (priceLists.size() > 1) {
            tvMin.setText(Double.toString(priceList[0]) + " - ");
            tvMax.setText(Double.toString(priceList[priceList.length - 1]));
        } else {
            tvMin.setText(Double.toString(priceList[0]));
        }

        progressBarDismiss();
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
        btnBuy.setOnClickListener(this);
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

    public void refresh() {
        String uuid = entitySaleDetailed.getContent().getActivityId();

        if (NetUtil.checkNetwork(this)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.getSaleDetailed(0, uuid, new OnHttpResponseListener() {
                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                    if(!StringUtil.isEmpty(resultJson)){
                        EntitySaleDetailed entitySaleDetailed =  JSON.parseObject(resultJson,EntitySaleDetailed.class);
                        if(entitySaleDetailed.isSuccess()){
                            //成功
                            //showShortToast(R.string.getSuccess);

                            progressBarDismiss();

                        }else{//显示失败信息
                            if (entitySaleDetailed.getCode().equals("401")) {
                                showShortToast(R.string.tokenInvalid);
                                toActivity(MainActivity.createIntent(context));
                            } else {
                                showShortToast(entitySaleDetailed.getMessage());
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
