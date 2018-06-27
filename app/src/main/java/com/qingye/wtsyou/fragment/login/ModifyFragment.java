package com.qingye.wtsyou.fragment.login;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.MainTabActivity;
import com.qingye.wtsyou.activity.search.SelectStarsActivity;
import com.qingye.wtsyou.basemodel.ErrorCodeTool;
import com.qingye.wtsyou.manager.HttpManager;
import com.qingye.wtsyou.manager.HttpModel;
import com.qingye.wtsyou.model.EntityBooleanContent;
import com.qingye.wtsyou.model.EntityLogin;
import com.qingye.wtsyou.model.EntityPersonalMessage;
import com.qingye.wtsyou.model.EntityStringContent;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.utils.URLConstant;
import com.qingye.wtsyou.widget.CountButton;
import com.qingye.wtsyou.widget.VerticalViewPager;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.interfaces.IErrorCodeTool;
import zuo.biao.library.model.EntityBase;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.HttpRequest.URL_BASE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {

    private TextView tvTextView;
    private Button btnRegister;
    private Button btnConfirm;
    private EditText edtPhone;
    private EditText edtCode;
    private EditText edtPassword;

    private ImageView ivDownLeft,ivDownRight;
    private View line;

    private String checkIndex;
    private CountButton btnGetVerifyCode;

    private ImageView ivQQ;
    private ImageView ivWeiXin;
    private ImageView ivSina;

    private LinearLayout llArea;
    private LinearLayout llAgreement;

    private CustomDialog progressBar;

    private HttpModel<EntityLogin> mEntityLoginHttpModel;
    private HttpModel<EntityBooleanContent> mEntityFirstLoginHttpModel;
    private HttpModel<EntityPersonalMessage> mEntityPersonalMessageHttpModel;
    private HttpModel<EntityStringContent> mVerifyCodeHttpModel;
    private HttpModel<EntityStringContent> mModifyHttpModel;

    private int Id = 0;

    public ModifyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_register);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        Id = ((MainActivity) getActivity()).getId();

        //登录
        mEntityLoginHttpModel = new HttpModel<>(EntityLogin.class);
        //第一次登录
        mEntityFirstLoginHttpModel = new HttpModel<>(EntityBooleanContent.class);
        //获取个人信息
        mEntityPersonalMessageHttpModel = new HttpModel<>(EntityPersonalMessage.class);
        //获取验证码
        mVerifyCodeHttpModel = new HttpModel<>(EntityStringContent.class);
        //重设密码
        mModifyHttpModel = new HttpModel<>(EntityStringContent.class);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        return view;
    }

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static ModifyFragment createInstance() {
        return new ModifyFragment();
    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }

    @Override
    public void initView() {
        tvTextView = findViewById(R.id.text_view);
        tvTextView.setText("点击登录");

        btnRegister = findViewById(R.id.btn_register);
        btnConfirm = findViewById(R.id.btn_confirm);
        ivDownLeft = findViewById(R.id.iv_down_left);
        ivDownRight = findViewById(R.id.iv_down_right);

        edtPhone = findViewById(R.id.edt_phone);
        edtCode = findViewById(R.id.edt_verifyCode);
        edtPassword = findViewById(R.id.edt_password);

        btnGetVerifyCode = findViewById(R.id.btn_getVerifyCode);
        btnGetVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mobile = edtPhone.getText().toString().trim();
                boolean checkPhone = checkPhone(mobile);
                //检查手机号
                if (checkPhone) {
                    //检查网络
                    if (NetUtil.checkNetwork(context)) {
                        getVerifyCode(mobile);
                    } else {
                        showShortToast(R.string.checkNetwork);
                    }
                }
            }
        });

        line = findViewById(R.id.line);
        llArea = findViewById(R.id.ll_select_area);
        llAgreement = findViewById(R.id.ll_agreement);

        line.setVisibility(View.GONE);
        llArea.setVisibility(View.GONE);
        llAgreement.setVisibility(View.GONE);
        btnRegister.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.VISIBLE);

        ivQQ = findViewById(R.id.iv_qq);
        ivWeiXin = findViewById(R.id.iv_weixin);
        ivSina = findViewById(R.id.iv_weibo);
    }

    @Override
    public void initData() {

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

    @Override
    public void initEvent() {
        btnRegister.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        ivDownLeft.setOnClickListener(this);
        ivDownRight.setOnClickListener(this);

        ivQQ.setOnClickListener(this);
        ivWeiXin.setOnClickListener(this);
        ivSina.setOnClickListener(this);
    }

    public void UMListener (final SHARE_MEDIA type) {

        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(getActivity()).setShareConfig(config);

        String accountType = null;
        if (type.equals(SHARE_MEDIA.QQ)) {
            accountType = "qq";
        }
        else if (type.equals(SHARE_MEDIA.WEIXIN)) {
            accountType = "weixin";
        }
        else if (type.equals(SHARE_MEDIA.SINA)) {
            accountType = "weibo";
        }

        final String finalAccountType = accountType;

        UMAuthListener authListener = new UMAuthListener() {
            /**
             * @param platform 平台名称
             * @desc 授权开始的回调
             */
            @Override
            public void onStart(SHARE_MEDIA platform) {
            }

            /**
             * @param platform 平台名称
             * @param action   行为序号，开发者用不上
             * @param data     用户资料返回
             * @desc 授权成功的回调
             */
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                StringBuffer sb = new StringBuffer();
                for (String key : data.keySet()) {
                    sb.append(key + ":" + data.get(key) + ",");
                }

                String loginId = data.get("uid");
                String photo = data.get("iconurl");
                String nickname = data.get("name");
                login(finalAccountType, loginId, null, photo, nickname);

                Toast.makeText(getActivity(), "授权成功", Toast.LENGTH_SHORT).show();

            }

            /**
             * @param platform 平台名称
             * @param action   行为序号，开发者用不上
             * @param t        错误原因
             * @desc 授权失败的回调
             */
            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                Toast.makeText(getActivity(), "授权失败：" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

            /**
             * @param platform 平台名称
             * @param action   行为序号，开发者用不上
             * @desc 授权取消的回调
             */
            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                Toast.makeText(getActivity(), "授权取消", Toast.LENGTH_SHORT).show();
            }
        };

        UMShareAPI.get(getActivity()).deleteOauth(getActivity(), type,null);
        UMShareAPI.get(getActivity()).getPlatformInfo(getActivity(), type, authListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                final MainActivity mainActivity1 = (MainActivity) getActivity();
                mainActivity1.setFragment2Fragment(new MainActivity.Fragment2Fragment() {
                    @Override
                    public void gotoFragment(VerticalViewPager viewPager) {
                        viewPager.setCurrentItem(0);
                    }
                });
                mainActivity1.forSkip();
                break;
            case R.id.btn_confirm:
                modify();
                break;
            case R.id.iv_down_left:
                final MainActivity mainActivity3 = (MainActivity) getActivity();
                mainActivity3.setFragment2Fragment(new MainActivity.Fragment2Fragment() {
                    @Override
                    public void gotoFragment(VerticalViewPager viewPager) {
                        viewPager.setCurrentItem(0);
                    }
                });
                mainActivity3.forSkip();
                break;
            case R.id.iv_down_right:
                final MainActivity mainActivity4 = (MainActivity) getActivity();
                mainActivity4.setFragment2Fragment(new MainActivity.Fragment2Fragment() {
                    @Override
                    public void gotoFragment(VerticalViewPager viewPager) {
                        viewPager.setCurrentItem(0);
                    }
                });
                mainActivity4.forSkip();
                break;
            case R.id.iv_qq:
                UMListener(SHARE_MEDIA.QQ);
                break;
            case R.id.iv_weixin:
                UMListener(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.iv_weibo:
                UMListener(SHARE_MEDIA.SINA);
                break;
            default:
                break;
        }
    }

    private boolean checkPhone(String mobile) {

        if(TextUtils.isEmpty(mobile)){
            showShortToast(R.string.editPhone);
            return false;
        } else {
            if (mobile.length() < 11) {
                showShortToast(R.string.checkPhone);
                return false;
            } else {
                return true;
            }
        }
    }

    private void getVerifyCode(String mobile) {

        String type = "changPassword";
        String request = HttpRequest.postGetVerifyCode(type, mobile);
        mVerifyCodeHttpModel.post( request,URL_BASE + URLConstant.OTHERVERIFYCODE
                ,4,ModifyFragment.this);
    }

    public void login(String accountType, String loginId, String password, String photo, String nickname) {

        setProgressBar();
        progressBar.show();

        String request = HttpRequest.postLogin(accountType, loginId, password, photo, nickname);
        mEntityLoginHttpModel.post( request, URL_BASE + URLConstant.LOGIN,1,ModifyFragment.this);

    }

    public void modify() {
        setProgressBar();
        progressBar.show();

        String code = edtCode.getText().toString().trim();
        String type = "changPassword";
        String target = edtPhone.getText().toString().trim();
        String secondPwd = edtPassword.getText().toString().trim();
        String request = HttpRequest.postModify(checkIndex, code, type, target, secondPwd, secondPwd);
        mEntityLoginHttpModel.post( request, URL_BASE + URLConstant.MODIFY,5,ModifyFragment.this);
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
                EntityLogin entityLogin = mEntityLoginHttpModel.getData();
                //成功
                showShortToast(R.string.loginSuccess);
                //保存token信息
                HttpManager.getInstance().saveToken(entityLogin.getContent());

                //第一次登录请求判断
                mEntityFirstLoginHttpModel.get(URL_BASE + URLConstant.ISFIRSTLOGIN,2,ModifyFragment.this);
                mEntityPersonalMessageHttpModel.get(URL_BASE + URLConstant.GETPERSONALMESSAGE,3,this);
                break;
            case 2:
                if (mEntityFirstLoginHttpModel.getData().getContent()) {
                    toActivity(SelectStarsActivity.createIntent(context, 2));
                } else {
                    toActivity(MainTabActivity.createIntent(context));
                }
                break;
            case 3:
                EntityPersonalMessage entityPersonalMessage = mEntityPersonalMessageHttpModel.getData();
                //保存userId
                HttpManager.getInstance().saveUserId(entityPersonalMessage.getContent().getUserId());
                break;
            case 4:
                //成功
                showShortToast(R.string.sendSuccess);
                btnGetVerifyCode.start();

                checkIndex = mVerifyCodeHttpModel.getData().getContent();
                break;
            case 5:
                final MainActivity mainActivity2 = (MainActivity) getActivity();
                mainActivity2.setFragment2Fragment(new MainActivity.Fragment2Fragment() {
                    @Override
                    public void gotoFragment(VerticalViewPager viewPager) {
                        viewPager.setCurrentItem(0);
                    }
                });
                mainActivity2.forSkip();
                break;
        }
    }


    @Override
    public void ProgressDismiss(String url, int RequestCode) {
        progressBarDismiss();
    }
}
