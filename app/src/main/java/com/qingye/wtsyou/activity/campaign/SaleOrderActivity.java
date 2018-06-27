package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.reflect.TypeToken;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.home.RuleActivity;
import com.qingye.wtsyou.activity.my.CreateAddressActivity;
import com.qingye.wtsyou.adapter.campaign.SelectAddressAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.basemodel.IdName;
import com.qingye.wtsyou.basemodel.POI;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.Card;
import com.qingye.wtsyou.model.DeliveryAddress;
import com.qingye.wtsyou.model.EntityContent;
import com.qingye.wtsyou.model.EntityPaymentConfig;
import com.qingye.wtsyou.model.EntitySaleDetailed;
import com.qingye.wtsyou.model.PriceList;
import com.qingye.wtsyou.model.PriceListItem;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.GsonUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.campaign.SelectAddressView;
import com.qingye.wtsyou.view.campaign.SelectAddressView.OnItemChildClickListener;
import com.qingye.wtsyou.widget.AutoLineFeedLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;
import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class SaleOrderActivity extends BaseHttpRecyclerActivity<DeliveryAddress,SelectAddressView,SelectAddressAdapter>
        implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener, OnItemChildClickListener {

    private TextView tvHead;
    private ImageView ivBack;
    private LinearLayout btnDetailed;
    private LinearLayout llBottom;

    //活动名称
    private TextView tvName;
    private TextView tvAddress;
    private TextView tvTime;

    //购票信息
    private EditText edtNumber;
    private int number = 1;
    private TextView tvTotal;
    private double total;
    private double originalTotal;
    private double price;
    //优惠券
    private LinearLayout llCard;
    private TextView tvSelectCard;
    private Card selectCard;
    private Double disAmount = Double.valueOf(0);
    //明细
    private LinearLayout llDetailed;
    private Button btnGone;
    private TextView tvDetailNumber;
    private TextView tvDetailedPrice;
    private RelativeLayout rlCardDiscount;
    private TextView tvDetailedDiscount;
    //private TextView tvDetailedFare;
    private TextView tvDetailedTotal;

    private TextView tvAgreement;
    private TextView tvPay;

    private View popUpView;
    private PopupWindow payWayWin;
    private RadioGroup radioGroup;
    private RadioButton rbtWei,rbtAli;
    private TextView tvSelect;
    private Button btnClose;

    //默认
    private TextView tvDefaultName;
    private TextView tvDefaultPhone;
    private TextView tvDefaultAddress;
    //private TextView tvFare;
    //运费
    int fare = 0;

    private SelectAddressAdapter selectAddressAdapter;

    //地址显示区域
    private LinearLayout llAddress;
    //选择地址区域
    private LinearLayout llSelectAddress;
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvCreateNewAddress;
    private TextView tvCreateAddress;
    private Button btnCloseAddress;//地址弹窗

    private List<DeliveryAddress> deliveryAddressList = new ArrayList<>();

    DeliveryAddress defaultAddress = null;

    private ImageView ivArrow;
    private int narrowCount = 0;//记录明细是否显示

    //价格
    private EntitySaleDetailed entitySaleDetailed;
    private List<PriceListItem> priceListItem =  new ArrayList<>();
    private PriceList currentPriceList;
    private AutoLineFeedLayout layout;

    private CustomDialog progressBar;

    private HttpModel<EntityContent> mAddressHttpModel;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, EntitySaleDetailed entitySaleDetailed) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.SALEDETAILED, entitySaleDetailed);//放进数据流中
        return new Intent(context, SaleOrderActivity.class).putExtras(bundle);
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
                getAddress();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_order,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BroadcastAction.ACTION_ADDRESS_REFRESH);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);

        intent = getIntent();
        entitySaleDetailed = (EntitySaleDetailed) intent.getSerializableExtra(Constant.SALEDETAILED);

        setProgressBar();
        progressBar.show();

        //地址
        mAddressHttpModel = new HttpModel<>(EntityContent.class);
        getAddress();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        //价格
        getPriceListItem();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>


        srlBaseHttpRecycler.autoRefresh();
        /*srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(false);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(false);//尾部*/
    }

    public void getPriceListItem() {
        List<PriceList> priceLists = entitySaleDetailed.getContent().getPriceList();
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
        /*total = number.multiply(price).doubleValue();
        tvTotal.setText(Double.toString(total));*/
        getTotalValue();
        selectPrice();
    }

    public void selectPrice() {
        View view;
        layout.removeAllViews();
        final List<TextView> buttonList = new ArrayList<>();
        for (int i = 0; i < priceListItem.size(); i ++) {
            view = LayoutInflater.from(SaleOrderActivity.this).inflate(R.layout.list_radiobutton, null);
            final TextView button = (TextView) view.findViewById(R.id.btn);
            button.setTag(i);
            double allBig = priceListItem.get(i).getPriceList().getPrice();
            double allDou = allBig;
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

                            number = new Integer(edtNumber.getText().toString());
                            getTotalValue();
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

    public void getTotalValue() {
        if (selectCard != null) {
            disAmount = selectCard.getDiscountAmount();
            rlCardDiscount.setVisibility(View.VISIBLE);
            tvDetailedDiscount.setText("" + disAmount);
        } else {
            rlCardDiscount.setVisibility(View.GONE);
        }

        tvDetailNumber.setText("" + number);
        tvDetailedPrice.setText("" + price);
        //tvDetailedFare.setText("" + fare);

        originalTotal = number * price + fare;
        total = originalTotal - disAmount;
        tvTotal.setText(Double.toString(total));
        tvDetailedTotal.setText("" + total);
    }

    @Override
    public void initView() {
        super.initView();
        ivBack = findView(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("订单确认");

        //活动名称
        tvName = findView(R.id.tv_ticket_name);
        //地址
        tvAddress = findView(R.id.tv_ticket_address);
        //时间
        tvTime = findView(R.id.tv_ticket_time);
        //数量
        edtNumber = findView(R.id.edt_number_count);
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
                        number = new Integer(s.toString());
                        getTotalValue();
                    }
                } else {

                }
            }
        };
        edtNumber.addTextChangedListener(watcher);
        edtNumber.setTag(watcher);
        //优惠券
        llCard = findView(R.id.llCard);
        tvSelectCard = findView(R.id.tv_selected_card);

        //默认
        tvDefaultName = findView(R.id.tv_default_contact_name);
        tvDefaultPhone = findView(R.id.tv_default_contact_phone);
        tvDefaultAddress = findView(R.id.tv_default_delivery_address);
        //运费
        //tvFare = findView(R.id.tv_fare);

        //总价
        tvTotal = findView(R.id.tv_total);
        layout = findView(R.id.al);

        //查看明细
        btnDetailed = findView(R.id.btn_detailed);
        llBottom = findView(R.id.ll_bottom);
        ivArrow = findView(R.id.iv_arrow);
        llDetailed = findView(R.id.llDetailed);
        btnGone = findView(R.id.gone);
        //明细里面的详细内容
        tvDetailNumber = findView(R.id.tv_detailed_number);
        tvDetailedPrice = findView(R.id.tv_detailed_price);
        rlCardDiscount = findView(R.id.rlCardDiscount);
        tvDetailedDiscount = findView(R.id.tv_detailed_card_price);
        //tvDetailedFare = findView(R.id.tv_detailed_fare);
        tvDetailedTotal = findView(R.id.tv_detailed_total);

        llAddress = findView(R.id.llAddress);
        //选择地址
        llSelectAddress = findView(R.id.llSelectAddress);
        tvCancel = findView(R.id.tv_cancel);
        tvConfirm = findView(R.id.tv_confirm);
        //没地址时添加地址
        tvCreateAddress = findView(R.id.tv_create_address);
        //添加新的地址
        tvCreateNewAddress = findView(R.id.tv_create_new_address);
        btnCloseAddress = findView(R.id.btn_close_address);

        tvPay = findView(R.id.tv_pay);
        popUpView = getLayoutInflater().inflate(R.layout.popupwindow_pay_way, null);
        payWayWin = new PopupWindow(popUpView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        payWayWin.setOutsideTouchable(true);
        radioGroup = (RadioGroup) popUpView.findViewById(R.id.radio_group);
        rbtWei = (RadioButton) popUpView.findViewById(R.id.rbtn_wei);
        rbtAli = (RadioButton) popUpView.findViewById(R.id.rbtn_ali);
        tvSelect = (TextView) popUpView.findViewById(R.id.tv_select);
        btnClose = (Button) popUpView.findViewById(R.id.btn_close);
        radioGroup.setOnCheckedChangeListener(new WayGrouplistener());

        tvAgreement = findView(R.id.tv_agreement);
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
    public void setList(final List<DeliveryAddress> list) {

        setList(new AdapterCallBack<SelectAddressAdapter>() {

            @Override
            public SelectAddressAdapter createAdapter() {

                selectAddressAdapter = new SelectAddressAdapter(context);
                selectAddressAdapter.setOnItemChildClickListener(SaleOrderActivity.this);

                return selectAddressAdapter;
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        //活动名称
        tvName.setText(entitySaleDetailed.getContent().getActivityName());
        //地址
        /*String province = entitySaleDetailed.getContent().getAddress().getPcdt().getProvince();
        String city = entitySaleDetailed.getContent().getAddress().getPcdt().getCity();
        String district = entitySaleDetailed.getContent().getAddress().getPcdt().getDistrict();
        String township = entitySaleDetailed.getContent().getAddress().getPcdt().getTownship();
        String address = entitySaleDetailed.getContent().getAddress().getAddress();*/
        tvAddress.setText(entitySaleDetailed.getContent().getStadiumsName());
        //时间
        tvTime.setText(entitySaleDetailed.getContent().getStartTimeStr());

        if (deliveryAddressList.size() > 0) {
            tvCreateAddress.setVisibility(View.GONE);
            llAddress.setVisibility(View.VISIBLE);
            getSelectAddress();
        } else {
            tvCreateAddress.setVisibility(View.VISIBLE);
            llAddress.setVisibility(View.GONE);
        }

        //tvFare.setText(Integer.toString(fare));

        progressBarDismiss();
    }

    public void getSelectAddress() {

        if (deliveryAddressList.size() > 0) {
            for (DeliveryAddress deliveryAddress : deliveryAddressList) {
                if (deliveryAddress.getDefault()) {
                    defaultAddress = deliveryAddress;
                }
            }

            //名字
            tvDefaultName.setText(defaultAddress.getName());
            //联系电话
            tvDefaultPhone.setText(defaultAddress.getMobile());
            //省
            String defaultProvince = defaultAddress.getPoi().getPcdt().getProvince();
            //市
            String defaultCity = defaultAddress.getPoi().getPcdt().getCity();
            //区
            String defaultDistrict = defaultAddress.getPoi().getPcdt().getDistrict();
            //街道
            String defaultTownship = defaultAddress.getPoi().getPcdt().getTownship();
            //详细
            String defaultDetail = defaultAddress.getPoi().getAddress();
            //地址
            tvDefaultAddress.setText(defaultProvince + defaultCity + defaultDistrict + defaultTownship + defaultDetail);
        } else {
            return;
        }
    }
    @Override
    public void getListAsync(int page) {

    }

    @Override
    public List<DeliveryAddress> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
        btnDetailed.setOnClickListener(this);
        btnGone.setOnClickListener(this);
        llAddress.setOnClickListener(this);
        llSelectAddress.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        tvCreateAddress.setOnClickListener(this);
        tvCreateNewAddress.setOnClickListener(this);
        btnCloseAddress.setOnClickListener(this);
        llCard.setOnClickListener(this);

        tvPay.setOnClickListener(this);
        tvSelect.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        tvAgreement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_detailed:
                Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.gradually);
                narrowCount ++;
                if (narrowCount % 2 == 0) {
                    llDetailed.setVisibility(View.GONE);
                    ivArrow.setImageResource(R.mipmap.next_k);
                } else {
                    llDetailed.setVisibility(View.VISIBLE);
                    llDetailed.startAnimation(animation1);
                    ivArrow.setImageResource(R.mipmap.next_l);
                }
                break;
            case R.id.gone:
                llDetailed.setVisibility(View.GONE);
                ivArrow.setImageResource(R.mipmap.next_k);
                narrowCount ++;
                break;
            case R.id.llAddress:
                Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.gradually);
                llSelectAddress.setVisibility(View.VISIBLE);
                llSelectAddress.setAnimation(animation2);
                break;
            case R.id.tv_cancel:
                llSelectAddress.setVisibility(View.GONE);
                break;
            case R.id.tv_confirm:
                getSelectAddress();
                llSelectAddress.setVisibility(View.GONE);
                break;
            case R.id.tv_create_new_address:
                toActivity(CreateAddressActivity.createIntent(context));
                break;
            case R.id.tv_create_address:
                toActivity(CreateAddressActivity.createIntent(context));
                break;
            case R.id.btn_close_address:
                llSelectAddress.setVisibility(View.GONE);
                break;
            case R.id.tv_pay:
                payWayWin.setOutsideTouchable(true);
                payWayWin.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                payWayWin.update(0, 0, RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                break;
            case R.id.tv_select:
                if (rbtWei.isChecked()) {

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
                payWayWin.dismiss();
                break;
            case R.id.btn_close:
                payWayWin.dismiss();
                break;
            case R.id.llCard:
                toActivity(CardCanUseActivity.createIntent(context, originalTotal), REQUEST_TO_SELECT_CARD);
                break;
            case R.id.tv_agreement:
                toActivity(RuleActivity.createIntent(context, "concertPay", "购票服务条款"));
                break;
            default:
                break;
        }
    }

    //类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private static final int REQUEST_TO_SELECT_CARD = 1;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TO_SELECT_CARD:
                if (data != null) {
                    selectCard = (Card) data.getExtras().getSerializable(Constant.SELECTCARD);
                    disAmount = selectCard.getDiscountAmount();
                    tvSelectCard.setText("已选择优惠券 ： 满" + selectCard.getMinAmount() + "减" + selectCard.getDiscountAmount());
                    getTotalValue();
                }
                break;
            default:
                break;
        }
    }

    private void pay(final IdName channel) {
        String[] perms = {android.Manifest.permission.READ_PHONE_STATE,};
        String uuid = entitySaleDetailed.getContent().getActivityId();
        BigDecimal carrieFree = BigDecimal.ZERO;
        if (defaultAddress == null ) {
            showShortToast(R.string.noDeliveryAddress);
            return;
        }
        POI poi = new POI();
        poi = defaultAddress.getPoi();
		/*LngLat lanLat = new LngLat();
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
		String phone = defaultAddress.getMobile();
		String receiver = defaultAddress.getName();
        String SkuId = currentPriceList.getUuid();
        if (EasyPermissions.hasPermissions(this, perms)) {

            //检查网络
            if (NetUtil.checkNetwork(context)) {
                setProgressBar();
                progressBar.show();

                HttpRequest.postGetPaymentConfigOrder(0, uuid, carrieFree, new BigDecimal(price) ,new BigDecimal(number),
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
                    PayTask alipay = new PayTask(SaleOrderActivity.this);
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

    @Override
    public void onSelectClick(View view, int position) {
        for (int i = 0;i < deliveryAddressList.size();i ++) {
            if (i == position) {
                deliveryAddressList.get(i).setDefault(true);
            } else {
                deliveryAddressList.get(i).setDefault(false);
            }
        }
        setList(deliveryAddressList);
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

    public void getAddress() {
        /*setProgressBar();
        progressBar.show();*/

        String request = HttpRequest.postGetAddress();
        //地址列表
        mAddressHttpModel.post(request, URL_BASE + URLConstant.GETRECEIVINGADDRESS, 1,this);
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
                EntityContent entityContent =  mAddressHttpModel.getData();
                deliveryAddressList = GsonUtil.getGson().fromJson(GsonUtil.getGson().toJson(entityContent.getContent())
                        ,new TypeToken<List<DeliveryAddress>>(){}.getType());

                if (deliveryAddressList.size() > 0) {
                    setList(deliveryAddressList);
                }

                srlBaseHttpRecycler.finishRefresh();
                srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();

                initData();
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        getAddress();
    }
}
