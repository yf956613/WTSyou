package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.adapter.my.RechargeAdapter;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.basemodel.IdName;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.EntityPaymentConfig;
import com.qingye.wtsyou.model.EntityRule;
import com.qingye.wtsyou.model.Recharge;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.view.my.RechargeView;

import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.widget.CustomDialog;
import com.qingye.wtsyou.widget.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;
import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class RechargeActivity extends BaseHttpRecyclerActivity<Recharge,RechargeView,RechargeAdapter> implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivLeft;
    private TextView tvHead;
    private Button btnRecharge;
    private TextView tvPayWay;

    private View popUpView;
    private PopupWindow payWayWin;
    private RadioGroup radioGroup;
    private RadioButton rbtWei,rbtAli;
    private TextView tvSelect;
    private WebView webView;

    private HttpModel<EntityRule> mEntityRuleHttpModel;

    private CustomDialog progressBar;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, RechargeActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechage,this);

        //规则
        mEntityRuleHttpModel = new HttpModel<>(EntityRule.class);
        ruleQuery();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //禁止滑动
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(context);
        linearLayoutManager.setScrollEnabled(false);
        rvBaseRecycler.setNestedScrollingEnabled(false);//解决卡顿
        rvBaseRecycler.setLayoutManager(linearLayoutManager);

        //srlBaseHttpRecycler.autoRefresh();
        srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(false);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(false);//尾部

        //实例化一个GridLayoutManager，列数为2
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        rvBaseRecycler.setLayoutManager(layoutManager);
    }

    @Override
    public void initView() {
        super.initView();
        ivLeft = findView(R.id.iv_left);
        ivLeft.setImageResource(R.mipmap.back_a);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("钻石充值");
        btnRecharge = findView(R.id.btn_recharge);

        tvPayWay = findView(R.id.tv_way);
        popUpView = getLayoutInflater().inflate(R.layout.popupwindow_pay_way, null);
        payWayWin = new PopupWindow(popUpView, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        payWayWin.setOutsideTouchable(true);
        radioGroup = (RadioGroup) popUpView.findViewById(R.id.radio_group);
        rbtWei = (RadioButton) popUpView.findViewById(R.id.rbtn_wei);
        rbtAli = (RadioButton) popUpView.findViewById(R.id.rbtn_ali);
        tvSelect = (TextView) popUpView.findViewById(R.id.tv_select);
        radioGroup.setOnCheckedChangeListener(new WayGrouplistener());
        webView = findView(R.id.webView);
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
        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);
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
    public void setList(List<Recharge> list) {
        final List<Recharge> templist = new ArrayList<>();
        for(int i = 1;i < 7;i ++) {
            Recharge recharge = new Recharge();
            templist.add(recharge);
        }
        //list.addAll(templist);
        setList(new AdapterCallBack<RechargeAdapter>() {

            @Override
            public RechargeAdapter createAdapter() {
                return new RechargeAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(templist);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void getListAsync(int page) {

    }

    @Override
    public List<Recharge> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivLeft.setOnClickListener(this);
        btnRecharge.setOnClickListener(this);
        tvPayWay.setOnClickListener(this);
        tvSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_way:
                payWayWin.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                payWayWin.update(0, 0, RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                break;
            case R.id.btn_recharge:
                IdName idName = new IdName();
                idName.setId("alipay_app");
                idName.setName("支付宝支付");
                //检查网络
                if (NetUtil.checkNetwork(context)) {
                    pay(idName);
                } else {
                    showShortToast(R.string.checkNetwork);
                }
                break;
            case R.id.tv_select:
                if (rbtWei.isChecked()) {
                    tvPayWay.setText("微信支付");
                } else {
                    tvPayWay.setText("支付宝支付");
                }
                payWayWin.dismiss();
                break;
            default:
                break;
        }
    }

    private void pay(final IdName channel) {
        String[] perms = {android.Manifest.permission.READ_PHONE_STATE,};
        if (EasyPermissions.hasPermissions(this, perms)) {

            //检查网络
            if (NetUtil.checkNetwork(context)) {
                setProgressBar();
                progressBar.show();

                    HttpRequest.postGetPaymentConfig(0, new Double(0.01), channel, new OnHttpResponseListener() {

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
                    EasyPermissions.requestPermissions(this, "支付需要手机。。。。权限",
                            0, perms);
                }
            } else {
                showShortToast(R.string.checkNetwork);
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
                    PayTask alipay = new PayTask(RechargeActivity.this);
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

    public void ruleQuery() {
        mEntityRuleHttpModel.get( URL_BASE + URLConstant.RULE + "cash",2,this);
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
                //成功
                showShortToast(R.string.createVoteSuccess);
                finish();
                break;
            case 2:
                WebSettings settings = webView.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setDomStorageEnabled(true);
                settings.setUseWideViewPort(true);
                settings.setLoadWithOverviewMode(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
                } else {
                    settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
                }

                settings.setDefaultFontSize(30);
                settings.setDefaultFixedFontSize(30);

                EntityRule entityRule = mEntityRuleHttpModel.getData();
                webView.setBackgroundColor(0); // 设置背景色
                webView.loadData(entityRule.getContent().getRuleDescription(), "text/html;charset=utf-8","utf-8");
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }
}
