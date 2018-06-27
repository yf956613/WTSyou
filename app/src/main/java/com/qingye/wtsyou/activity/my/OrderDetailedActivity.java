package com.qingye.wtsyou.activity.my;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.EntityClassContent;
import com.qingye.wtsyou.model.EntityOrderDetailed;
import com.qingye.wtsyou.model.EntityRepay;
import com.qingye.wtsyou.model.EntityStringContent;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;

import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import pub.devrel.easypermissions.EasyPermissions;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.ui.BottomMenuWindow;
import zuo.biao.library.widget.CustomDialog;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;
import static com.umeng.socialize.utils.ContextUtil.getContext;

/**
 * Created by pm89 on 2018/6/26.
 */

public class OrderDetailedActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack;
    private TextView tvHead;
    //配送信息
    private TextView tvContactName;
    private TextView tvContactPhone;
    private TextView tvDeliveryAddress;
    //订单
    private TextView tvOrderId;
    private ImageView ivImg;
    private TextView tvActivityName;
    private TextView tvPrice;
    //订单明细
    private TextView tvDetailNumber;
    private TextView tvDetailedPrice;
    private RelativeLayout rlCardDiscount;
    private TextView tvDetailedDiscount;
    //private TextView tvDetailedFare;
    private TextView tvDetailedTotal;

    //操作按钮
    private TextView tvCancel;//取消
    private TextView tvCall;//电话咨询
    private TextView tvRemind;//提醒发货
    private TextView tvPay;//付款
    private TextView tvTake;//收货
    private TextView tvOther;//其他状态

    private View popUpView;
    private PopupWindow payWayWin;
    private RadioGroup radioGroup;
    private RadioButton rbtWei,rbtAli;
    private TextView tvSelect;
    private Button btnClose;//支付弹窗

    private SwipeRefreshLayout swipeRefresh;
    private CustomDialog progressBar;

    private EntityOrderDetailed entityOrderDetailed;

    private HttpModel<EntityOrderDetailed> mDetailedHttpModel;
    private HttpModel<EntityClassContent> mCancelHttpModel;
    private HttpModel<EntityRepay> mRepayHttpModel;
    private HttpModel<EntityStringContent> mTakeHttpModel;

    public String orderId;
    private String mobile;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, String orderId) {
        return new Intent(context, OrderDetailedActivity.class).putExtra(Constant.ORDERID, orderId);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detailed,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        intent = getIntent();
        orderId = intent.getStringExtra(Constant.ORDERID);

        //订单详情
        mDetailedHttpModel = new HttpModel<>(EntityOrderDetailed.class);
        orderDetailQuery();
        //取消订单
        mCancelHttpModel = new HttpModel<>(EntityClassContent.class);
        //付款
        mRepayHttpModel = new HttpModel<>(EntityRepay.class);
        //收货
        mTakeHttpModel = new HttpModel<>(EntityStringContent.class);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //刷新
        initHrvsr();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
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

    @Override
    public void initView() {
        swipeRefresh = findView(R.id.swipe_refresh_widget);

        ivBack = findView(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("订单详情");

        //配送信息
        tvContactName = findView(R.id.tv_contact_name);
        tvContactPhone = findView(R.id.tv_contact_phone);
        tvDeliveryAddress = findView(R.id.tv_delivery_address);
        //订单信息
        tvOrderId = findView(R.id.tv_order_number);
        ivImg = findView(R.id.iv_order_img);
        tvActivityName = findView(R.id.tv_order_name);
        tvPrice = findView(R.id.tv_price);
        //订单明细
        //明细里面的详细内容
        tvDetailNumber = findView(R.id.tv_detailed_number);
        tvDetailedPrice = findView(R.id.tv_detailed_price);
        rlCardDiscount = findView(R.id.rlCardDiscount);
        tvDetailedDiscount = findView(R.id.tv_detailed_card_price);
        //tvDetailedFare = findView(R.id.tv_detailed_fare);
        tvDetailedTotal = findView(R.id.tv_detailed_total);

        //操作按钮
        tvCancel = findView(R.id.tv_cancel);
        tvCall = findView(R.id.tv_query_logistics);
        tvRemind = findView(R.id.tv_reminding);
        tvPay = findView(R.id.tv_pay);
        tvTake = findView(R.id.tv_take);
        tvOther = findView(R.id.tv_other);

        popUpView = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_pay_way, null);
        payWayWin = new PopupWindow(popUpView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        payWayWin.setOutsideTouchable(true);
        radioGroup = (RadioGroup) popUpView.findViewById(R.id.radio_group);
        rbtWei = (RadioButton) popUpView.findViewById(R.id.rbtn_wei);
        rbtAli = (RadioButton) popUpView.findViewById(R.id.rbtn_ali);
        tvSelect = (TextView) popUpView.findViewById(R.id.tv_select);
        btnClose = (Button) popUpView.findViewById(R.id.btn_close);
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

    private static final String[] SEX_ITEM = {"取消", "确定"};

    private static final int REQUEST_TO_BOTTOM_MENU = 31;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TO_BOTTOM_MENU:
                if (data != null) {
                    int selectedPosition = data.getIntExtra(BottomMenuWindow.RESULT_ITEM_ID, -1);
                    if (selectedPosition >= 0 && selectedPosition < SEX_ITEM.length) {
                        if (selectedPosition == 1) {
                            cancelOrder();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void initData() {
        if (isAlive()) {
            //配送信息
            tvContactName.setText(entityOrderDetailed.getContent().getReveiver());
            tvContactPhone.setText(entityOrderDetailed.getContent().getMobile());
            //省
            String province = entityOrderDetailed.getContent().getReveiveAddress().getPcdt().getProvince();
            //市
            String city = entityOrderDetailed.getContent().getReveiveAddress().getPcdt().getCity();
            //区
            String district = entityOrderDetailed.getContent().getReveiveAddress().getPcdt().getDistrict();
            //街道
            String township = entityOrderDetailed.getContent().getReveiveAddress().getPcdt().getTownship();
            //poi
            //String poiString = data.getPoi().getPoiAddress() + data.getPoi().getPoiName();
            //详细
            String detail = entityOrderDetailed.getContent().getReveiveAddress().getAddress();

            tvDeliveryAddress.setText(province + city + district + township + detail);

            //订单信息
            tvOrderId.setText(entityOrderDetailed.getContent().getId());
            //图片
            String url = entityOrderDetailed.getContent().getActivitySimple().getActivityPic();
            if (url != null) {
                Glide.with(context)
                        .load(url)
                        .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                        .into(ivImg);
            }
            tvActivityName.setText(entityOrderDetailed.getContent().getActivitySimple().getActivityName());
            //单价
            tvPrice.setText(Double.toString(entityOrderDetailed.getContent().getPrice()));

            String state = entityOrderDetailed.getContent().getState();
            //待付款
            if (state.equals("submitted")) {
                tvCancel.setVisibility(View.VISIBLE);
                tvCall.setVisibility(View.GONE);
                tvRemind.setVisibility(View.GONE);
                tvPay.setVisibility(View.VISIBLE);
                tvTake.setVisibility(View.GONE);
                tvOther.setVisibility(View.GONE);
            }
            //待发货
            else if (state.equals("unShip")) {
                tvCancel.setVisibility(View.GONE);
                tvCall.setVisibility(View.VISIBLE);
                tvRemind.setVisibility(View.VISIBLE);
                tvPay.setVisibility(View.GONE);
                tvTake.setVisibility(View.GONE);
                tvOther.setVisibility(View.GONE);
            }
            //待收货
            else if (state.equals("shipped")) {
                tvCancel.setVisibility(View.GONE);
                tvCall.setVisibility(View.VISIBLE);
                tvRemind.setVisibility(View.VISIBLE);
                tvPay.setVisibility(View.GONE);
                tvTake.setVisibility(View.VISIBLE);
                tvOther.setVisibility(View.GONE);
            }
            //订单取消
            else if (state.equals("cancel")) {
                tvCancel.setVisibility(View.GONE);
                tvCall.setVisibility(View.GONE);
                tvRemind.setVisibility(View.GONE);
                tvPay.setVisibility(View.GONE);
                tvTake.setVisibility(View.GONE);
                tvOther.setVisibility(View.VISIBLE);
                tvOther.setText("订单已取消");
                tvOther.setBackground(null);
                tvOther.setTextColor(getResources().getColor(R.color.black_text2));
            }
            //订单完成
            else if (state.equals("finish")) {
                tvCancel.setVisibility(View.GONE);
                tvCall.setVisibility(View.GONE);
                tvRemind.setVisibility(View.GONE);
                tvPay.setVisibility(View.GONE);
                tvTake.setVisibility(View.GONE);
                tvOther.setVisibility(View.VISIBLE);
                tvOther.setText("订单完成");
                tvOther.setBackground(null);
                tvOther.setTextColor(getResources().getColor(R.color.orange_text2));
            }

            tvDetailNumber.setText("" + entityOrderDetailed.getContent().getQty());
            tvDetailedPrice.setText("" + entityOrderDetailed.getContent().getQty() *
                                          entityOrderDetailed.getContent().getPrice());
            //tvDetailedFare.setText("" + fare);

            tvDetailedTotal.setText("" + entityOrderDetailed.getContent().getTotal());
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
                        orderDetailQuery();
                        showShortToast(R.string.getSuccess);
                        swipeRefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvCall.setOnClickListener(this);
        tvRemind.setOnClickListener(this);
        tvPay.setOnClickListener(this);
        tvTake.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                Intent intent2 = new Intent(
                        BroadcastAction.ACTION_ORDER_REFRESH);
                // 发送广播
                sendBroadcast(intent2);
                finish();
                break;
            case R.id.tv_cancel:
                toActivity(BottomMenuWindow.createIntent(context, SEX_ITEM)
                        .putExtra(BottomMenuWindow.INTENT_TITLE, "是否取消该订单？"), REQUEST_TO_BOTTOM_MENU, false);
                break;
            case R.id.tv_query_logistics:
                mobile = "10086";
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.CALL_PHONE},1);
                }else {
                    call();
                }
                break;
            case R.id.tv_reminding:
                showShortToast(R.string.remindSuccess);
                break;
            case R.id.tv_pay:
                payWayWin.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                payWayWin.update(0, 0, RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                break;
            case R.id.tv_take:
                takeOrder();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }



    public void orderDetailQuery() {
        setProgressBar();
        progressBar.show();

        mDetailedHttpModel.get(orderId,URL_BASE + URLConstant.ORDERDETAILED,1,this);
    }

    public void cancelOrder() {
        String request = HttpRequest.postCancelOrder(orderId);
        mCancelHttpModel.post(request, URL_BASE + URLConstant.ORDERCANCEL, 2,this);
    }

    public void rePayOrder() {
        String[] perms = {android.Manifest.permission.READ_PHONE_STATE,};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            String request = HttpRequest.postNoParams();
            mRepayHttpModel.post(request, URL_BASE + URLConstant.ORDERREPAY + orderId, 3,this);
        } else {
            EasyPermissions.requestPermissions(this, "支付需要手机。。。。权限",
                    0, perms);
        }
    }

    public void takeOrder() {
        String request = HttpRequest.postNoParams();
        mTakeHttpModel.post(request, URL_BASE + URLConstant.ORDERTAKE + orderId, 4,this);
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

    private void aliPay(final String parameters) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                try {
                    showShortToast("获取支付参数成功，开始调用支付");

                    if (parameters == null) {
                        showShortToast("未调用支付，支付参数为空");
                        return;
                    }
                    PayTask alipay = new PayTask(getActivity());
                    Map<String, String> result = alipay.payV2(parameters, true);

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

    @Override
    public IErrorCodeTool getErrorCodeTool() {
        return ErrorCodeTool.getInstance();
    }

    @Override
    public void Success(String url, int RequestCode, EntityBase entityBase) {
        super.Success(url, RequestCode, entityBase);
        switch (RequestCode) {
            case 1:
                entityOrderDetailed = mDetailedHttpModel.getData();
                initData();
                break;
            case 2:
                EntityClassContent.Content entityClassContent = mCancelHttpModel.getData().getContent();
                showShortToast(entityClassContent.getMessage());
                break;
            case 3:
                EntityRepay.Content repay = mRepayHttpModel.getData().getContent();
                String parameters = repay.getParameters().get(0).getValue();
                aliPay(parameters);
                break;
            case 4:
                showShortToast(R.string.takeSuccess);
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }

    private void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mobile));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int [] granResults) {
        switch (requestCode) {
            case 1:
                if (granResults.length > 0 && granResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    showShortToast("您没有打开拨号权限");
                }
                break;
            default:
        }
    }

}
