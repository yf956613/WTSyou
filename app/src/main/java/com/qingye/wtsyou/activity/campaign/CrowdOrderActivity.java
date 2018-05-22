package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.basemodel.IdName;
import com.qingye.wtsyou.basemodel.POI;
import com.qingye.wtsyou.modle.EntityCrowdDetailed;
import com.qingye.wtsyou.modle.EntityPaymentConfig;
import com.qingye.wtsyou.modle.PriceList;
import com.qingye.wtsyou.modle.PriceListItem;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.widget.AutoLineFeedLayout;
import com.qingye.wtsyou.widget.CustomDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

public class CrowdOrderActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private TextView tvHead;
    private ImageView ivBack;
    //活动名称
    private TextView tvName;
    private TextView tvAddress;
    private TextView tvTime;

    //众筹信息
    private EditText edtNumber;
    private BigDecimal number = BigDecimal.ONE;
    private TextView tvTotal;
    private double total;
    private BigDecimal price;

    private TextView tvPay;

    private View popUpView;
    private PopupWindow payWayWin;
    private RadioGroup radioGroup;
    private RadioButton rbtWei,rbtAli;
    private TextView tvSelect;

    //价格
    private EntityCrowdDetailed entityCrowdDetailed;
    private List<PriceListItem> priceListItem =  new ArrayList<>();
    private PriceList currnetPriceList;
    private AutoLineFeedLayout layout;

    private CustomDialog progressBar;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, EntityCrowdDetailed entityCrowdDetailed) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.CROWDDETAILED, entityCrowdDetailed);//放进数据流中
        return new Intent(context, CrowdOrderActivity.class).putExtras(bundle);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowd_order,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        setProgressBar();
        progressBar.show();

        intent = getIntent();
        entityCrowdDetailed = (EntityCrowdDetailed) intent.getSerializableExtra(Constant.CROWDDETAILED);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //价格
        getPriceListItem();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

    }

    public void getPriceListItem() {
        List<PriceList> priceLists = entityCrowdDetailed.getContent().getPriceList();
        for (PriceList priceList : priceLists) {
            PriceListItem priceItem = new PriceListItem();
            priceItem.setPriceList(priceList);
            priceItem.setSelector(false);
            priceListItem.add(priceItem);
        }
        //初始第一个未选择的价格
        price = priceListItem.get(0).getPriceList().getPrice();
        priceListItem.get(0).setSelector(true);
        currnetPriceList = priceListItem.get(0).getPriceList();
        total = number.multiply(price).doubleValue();
        tvTotal.setText(Double.toString(total));
        selectPrice();
    }

    public void selectPrice() {
        View view;
        final List<Button> buttonList = new ArrayList<>();
        for (int i = 0; i < priceListItem.size(); i ++) {
            view = LayoutInflater.from(CrowdOrderActivity.this).inflate(R.layout.list_radiobutton, null);
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
                            currnetPriceList = priceListItem.get(j).getPriceList();
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
        tvTotal.setText(Double.toString(total));
    }

    @Override
    public void initView() {
        ivBack = findViewById(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("众筹确认");

        //活动名称
        tvName = findViewById(R.id.tv_ticket_name);
        //地址
        tvAddress = findViewById(R.id.tv_ticket_address);
        //时间
        tvTime = findViewById(R.id.tv_ticket_time);
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

        //总价
        tvTotal = findViewById(R.id.tv_total);

        layout = findViewById(R.id.al);

        tvPay = findViewById(R.id.tv_pay);
        popUpView = getLayoutInflater().inflate(R.layout.popupwindow_pay_way, null);
        payWayWin = new PopupWindow(popUpView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        payWayWin.setOutsideTouchable(true);
        radioGroup = popUpView.findViewById(R.id.radio_group);
        rbtWei = popUpView.findViewById(R.id.rbtn_wei);
        rbtAli = popUpView.findViewById(R.id.rbtn_ali);
        tvSelect = popUpView.findViewById(R.id.tv_select);
        radioGroup.setOnCheckedChangeListener(new WayGrouplistener());
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

    @Override
    public void initData() {
        //活动名称
        tvName.setText(entityCrowdDetailed.getContent().getActivityName());
        //地址
        String province = entityCrowdDetailed.getContent().getAddress().getPcdt().getProvince();
        String city = entityCrowdDetailed.getContent().getAddress().getPcdt().getCity();
        String district = entityCrowdDetailed.getContent().getAddress().getPcdt().getDistrict();
        String township = entityCrowdDetailed.getContent().getAddress().getPcdt().getTownship();
        String address = entityCrowdDetailed.getContent().getAddress().getAddress();
        tvAddress.setText(province + city + district + township + address);
        //时间
        tvTime.setText(entityCrowdDetailed.getContent().getStartTimeStr());

        progressBarDismiss();
    }

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);

        tvPay.setOnClickListener(this);
        tvSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_pay:
                payWayWin.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                payWayWin.update(0, 0, RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                break;
            case R.id.tv_select:
                if (rbtWei.isChecked()) {

                }
                else if (rbtAli.isChecked()){
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
                payWayWin.dismiss();
                break;
            default:
                break;
        }
    }

    private void pay(final IdName channel) {
        String[] perms = {android.Manifest.permission.READ_PHONE_STATE,};
        String uuid = entityCrowdDetailed.getContent().getActivityId();
        BigDecimal carrieFree = BigDecimal.ZERO;
        final POI poi = new POI();
        String receiver = "陈佩斯";
        String phone = "18250711172";
        String SkuId = currnetPriceList.getUuid();
        if (EasyPermissions.hasPermissions(this, perms)) {

            //检查网络
            if (NetUtil.checkNetwork(context)) {
                setProgressBar();
                progressBar.show();

                HttpRequest.postGetPaymentConfigOrder(0, uuid, carrieFree, price, number,
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
                    PayTask alipay = new PayTask(CrowdOrderActivity.this);
                    Map<String, String> result = alipay.payV2(payInfo, true);

                    Intent intent = new Intent(
                            BroadcastAction.ACTION_CROWD_REFRESH);
                    // 发送广播
                    sendBroadcast(intent);

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
