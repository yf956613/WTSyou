package com.qingye.wtsyou.fragment.login;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.basemodel.EntityBase;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.widget.CountButton;
import com.qingye.wtsyou.widget.CustomDialog;
import com.qingye.wtsyou.widget.VerticalViewPager;

import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {

    private Button btnRegister;
    private Button btnConfirm;
    private ImageView ivDownLeft,ivDownRight;
    private EditText edtPhone;
    private EditText edtVerfiCode;
    private EditText edtPassword;
    private CheckBox agree;
    private CountButton btnGetVerifyCode;

    private LinearLayout llArea;
    private LinearLayout llAgreement;

    private int Id = 0;

    private CustomDialog progressBar;

    public RegisterFragment() {
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

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        /*//接收上一页传来的数据
        Bundle bundle = getArguments();
        Id = bundle.getInt("id");*/

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
    public static RegisterFragment createInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }

    @Override
    public void initView() {
        btnRegister = findViewById(R.id.btn_register);
        btnConfirm = findViewById(R.id.btn_confirm);
        ivDownLeft = findViewById(R.id.iv_down_left);
        ivDownRight = findViewById(R.id.iv_down_right);

        edtPhone = findViewById(R.id.edt_phone);
        edtVerfiCode = findViewById(R.id.edt_verifyCode);
        edtPassword = findViewById(R.id.edt_password);
        //是否同意
        agree = findViewById(R.id.cbtn_agreement);

        btnGetVerifyCode = findViewById(R.id.btn_getVerifyCode);

        llArea = findViewById(R.id.ll_select_area);
        llAgreement = findViewById(R.id.ll_agreement);

        if (Id == 0) {
            llArea.setVisibility(View.VISIBLE);
            llAgreement.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.VISIBLE);
            btnConfirm.setVisibility(View.GONE);
        } else {
            llArea.setVisibility(View.GONE);
            llAgreement.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.VISIBLE);
        }
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                //检查手机号
                final String mobile = edtPhone.getText().toString().trim();
                boolean checkPhone = checkPhone(mobile);
                if (checkPhone) {
                    register();
                }
                break;
            case R.id.btn_confirm:
                final MainActivity mainActivity2 = (MainActivity) getActivity();
                mainActivity2.setFragment2Fragment(new MainActivity.Fragment2Fragment() {
                    @Override
                    public void gotoFragment(VerticalViewPager viewPager) {
                        viewPager.setCurrentItem(0);
                    }
                });
                mainActivity2.forSkip();
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

        HttpRequest.getGetVerifyCode(0, mobile , new OnHttpResponseListener() {

            @Override
            public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                if(!StringUtil.isEmpty(resultJson)){
                    EntityBase entityBase =  JSON.parseObject(resultJson,EntityBase.class);
                    if(entityBase.isSuccess()){
                        //成功
                        showShortToast(R.string.sendSuccess);
                        btnGetVerifyCode.start();
                    }else{//显示失败信息
                        showShortToast(entityBase.getMessage());
                    }
                }else{
                    showShortToast(R.string.noReturn);
                }
            }
        });
    }

    private void register() {
        String verifyCode = edtVerfiCode.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(verifyCode)){
            showShortToast(R.string.editVerifyCode);
            return;
        }
        if (TextUtils.isEmpty(password)){
            showShortToast(R.string.editPassword);
            return;
        }

        if (agree.isChecked()) {


            //检查网络
            if (NetUtil.checkNetwork(context)) {

                    setProgressBar();
                    progressBar.show();

                    HttpRequest.postRegister(0,"mobile", edtPhone.getText().toString(), edtVerfiCode.getText().toString(),
                            edtPassword.getText().toString(), new OnHttpResponseListener() {

                                @Override
                                public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                                    if(!StringUtil.isEmpty(resultJson)){
                                        EntityBase entityBase =  JSON.parseObject(resultJson,EntityBase.class);
                                        if(entityBase.isSuccess()){
                                            //成功
                                            showShortToast(R.string.registerSuccess);

                                            progressBarDismiss();
                                        }else{//显示失败信息
                                            showShortToast(entityBase.getMessage());

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
                showShortToast(R.string.checkAgree);
            }


    }
}
