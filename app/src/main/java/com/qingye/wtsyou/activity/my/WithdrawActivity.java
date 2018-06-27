package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.model.EntityRule;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.URLConstant;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.model.EntityBase;
import com.qingye.wtsyou.manager.HttpModel;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class WithdrawActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivLeft;
    private TextView tvHead;
    private Button btnWithdraw;
    private EditText edtWithdraw;
    private WebView webView;

    private CustomDialog progressBar;

    private HttpModel<EntityBase> mEntityBaseHttpModel;
    private HttpModel<EntityRule> mEntityRuleHttpModel;

    private int coinTotal = 0;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, int coinTotal) {
        return new Intent(context, WithdrawActivity.class).putExtra(Constant.COINTOTAL, coinTotal);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw,this);

        //规则
        mEntityRuleHttpModel = new HttpModel<>(EntityRule.class);
        ruleQuery();

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        intent = getIntent();
        coinTotal = intent.getIntExtra(Constant.MINAMOUNT, 0);

        //提现申请
        mEntityBaseHttpModel = new HttpModel<>(EntityBase.class);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        ivLeft = findView(R.id.iv_left);
        ivLeft.setImageResource(R.mipmap.back_a);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("金币提现");
        btnWithdraw = findView(R.id.btn_withdraw);
        edtWithdraw = findView(R.id.edt_withdraw);
        webView = findView(R.id.webView);
    }

    @Override
    public void initData() {

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
    public void initEvent() {
        ivLeft.setOnClickListener(this);
        btnWithdraw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_withdraw:
                withDraw();
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    public void withDraw() {
        String amount = edtWithdraw.getText().toString().trim();
        if (TextUtils.isEmpty(amount)){
            showShortToast(R.string.noWithdraw);
            return;
        } else {
            if (Double.parseDouble(amount) == 0) {
                showShortToast(R.string.moreThanZero);
                return;
            }
            /*else if (Double.parseDouble(amount) < coinTotal) {
                showShortToast(R.string.noWithdrawEnough);
                return;
            }*/
        }
        String type = "alipay";

        setProgressBar();
        progressBar.show();

        String request = HttpRequest.postWithdraw(Integer.parseInt(amount), type);
        mEntityBaseHttpModel.post(request,URL_BASE + URLConstant.WITHDRAW,1,this);
    }

    public void ruleQuery() {
        mEntityRuleHttpModel.get( URL_BASE + URLConstant.RULE + "recharge",2,this);
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
                showShortToast(R.string.withdrawSuccess);
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
