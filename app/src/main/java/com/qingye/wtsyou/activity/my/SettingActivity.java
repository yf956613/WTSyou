package com.qingye.wtsyou.activity.my;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpManager;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.EntityPersonalMessage;
import com.qingye.wtsyou.utils.CacheUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.widget.SwitchView;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

public class SettingActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivLeft;
    private TextView tvHead;
    private RelativeLayout rlPhone;
    private TextView tvPhone;
    private RelativeLayout rlEmail;
    private TextView tvEmail;
    private RelativeLayout rlIdentify;
    private TextView tvReal;
    private RelativeLayout rlGesture;
    private RelativeLayout rlClean;
    private TextView tvCache;
    private RelativeLayout rlFeedBack;
    private RelativeLayout rlContact;
    private RelativeLayout rlAbout;
    private RelativeLayout rlQuit;

    private SwitchView switchView;
    private boolean isGesture = false;

    private String mobile;
    private String cache = "0";

    private String phone;
    private String email;
    private String real;

    private CustomDialog progressBar;

    private EntityPersonalMessage entityPersonalMessage;

    private HttpModel<EntityPersonalMessage> mEntityPersonalMessageHttpModel;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting, this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //获取个人信息
        mEntityPersonalMessageHttpModel = new HttpModel<>(EntityPersonalMessage.class);
        getPersonalMessage();

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        ivLeft = findView(R.id.iv_left);
        ivLeft.setImageResource(R.mipmap.back_a);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("设置");

        rlPhone = findView(R.id.rlPhone);
        tvPhone = findView(R.id.tv_phone);
        rlEmail = findView(R.id.rlEmail);
        tvEmail = findView(R.id.tv_email);
        rlIdentify = findView(R.id.rlIdentify);
        tvReal = findView(R.id.tv_real);
        rlGesture = findView(R.id.rlGesture);
        rlClean = findView(R.id.rlClean);
        tvCache = findView(R.id.tv_cache);
        rlFeedBack = findView(R.id.rlFeedBack);
        rlContact = findView(R.id.rlContact);
        rlAbout = findView(R.id.rlAbout);
        rlQuit = findView(R.id.rlQuit);

        switchView = findView(R.id.switch_gesture);
        switchView.setColor(0xFFFF7554, 0xFFFF7554);
        isGesture = HttpManager.getInstance().getGesture();
        switchView.setOpened(isGesture);
        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpManager.getInstance().saveGesture(switchView.isOpened());
            }
        });
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
    public void initData() {
        try {
            cache = CacheUtil.getTotalCacheSize(context);
            tvCache.setText(cache);
        } catch (Exception e) {
            e.printStackTrace();
        }

        phone = entityPersonalMessage.getContent().getMobile();
        if (phone == null) {
            tvPhone.setText(R.string.noBind);
        } else {
            tvPhone.setText(phone);
        }

        email = entityPersonalMessage.getContent().getEmail();
        if (email == null) {
            tvEmail.setText(R.string.noBind);
        } else {
            tvEmail.setText(R.string.bind);
        }

        real = entityPersonalMessage.getContent().getRealInfo();
        if (real == null) {
            tvReal.setText(R.string.noReal);
        } else {
            tvReal.setText(R.string.real);
        }
    }

    @Override
    public void initEvent() {
        ivLeft.setOnClickListener(this);
        rlPhone.setOnClickListener(this);
        rlEmail.setOnClickListener(this);
        rlIdentify.setOnClickListener(this);
        rlGesture.setOnClickListener(this);
        rlClean.setOnClickListener(this);
        rlFeedBack.setOnClickListener(this);
        rlContact.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
        rlQuit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.rlPhone:
                if (phone == null) {
                    toActivity(BindingPhoneActivity.createIntent(context));
                } else {
                    toActivity(ChangePhoneActivity.createIntent(context));
                }
                break;
            case R.id.rlEmail:
                if (email == null) {
                    toActivity(BindingEmailActivity.createIntent(context));
                } else {
                    toActivity(ChangeEmailActivity.createIntent(context));
                }
                break;
            case R.id.rlIdentify:
                toActivity(IdentifyActivity.createIntent(context));
                break;
            case R.id.rlGesture:
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, GestureActivity.class);
                SettingActivity.this.startActivity(intent);
                //toActivity(GestureActivity.createIntent(context));
                break;
            case R.id.rlClean:
                toActivity(CleanActivity.createIntent(context, cache));
                break;
            case R.id.rlFeedBack:
                toActivity(QuestionActivity.createIntent(context));
                break;
            case R.id.rlContact:
                mobile = "10086";
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.CALL_PHONE},1);
                }else {
                    call();
                }
                break;
            case R.id.rlAbout:
                toActivity(AboutActivity.createIntent(context));
                break;
            case R.id.rlQuit:
                postLoginOut();
                break;
            default:
                break;
        }
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

    private void postLoginOut() {
        if (NetUtil.checkNetwork(this)) {
            setProgressBar();
            progressBar.show();

            HttpRequest.postLoginOut(0, new OnHttpResponseListener() {

                @Override
                public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                    if(!StringUtil.isEmpty(resultJson)){
                        EntityBase entityBase =  JSON.parseObject(resultJson,EntityBase.class);
                        if(entityBase.isSuccess()){
                            //成功
                            showShortToast(R.string.loginOutSuccess);

                            toActivity(MainActivity.createIntent(context));

                            progressBarDismiss();
                        }else{

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

    public void getPersonalMessage() {
        setProgressBar();
        progressBar.show();

        mEntityPersonalMessageHttpModel.get(URL_BASE + URLConstant.GETPERSONALMESSAGE,1,this);
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
                entityPersonalMessage = mEntityPersonalMessageHttpModel.getData();
                initData();
                break;
        }
    }

    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }
}
