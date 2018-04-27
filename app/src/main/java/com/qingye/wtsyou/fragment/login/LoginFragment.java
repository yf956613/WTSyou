package com.qingye.wtsyou.fragment.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.MainActivity;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainTabActivity;
import com.qingye.wtsyou.activity.home.ChartsActivity;
import com.qingye.wtsyou.activity.search.SelectStarsActivity;
import com.qingye.wtsyou.adapter.home.HomePagerAdapter;
import com.qingye.wtsyou.utils.EntityLogin;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.widget.VerticalViewPager;

import me.relex.circleindicator.CircleIndicator;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.Log;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {

    private ImageView ivUpLeft,ivUpRight;//上拉
    private TextView tvModify;//忘记密码

    private EditText edtLoginId;//用户名
    private EditText edtPassword;//密码
    private Button btnLogin;//登录

    public LoginFragment() {
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
        setContentView(R.layout.fragment_login);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

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
    public static LoginFragment createInstance() {
        return new LoginFragment();
    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }

    @Override
    public void initView() {
        tvModify = findViewById(R.id.tv_modify_pwd);
        ivUpLeft = findViewById(R.id.iv_up_left);
        ivUpRight = findViewById(R.id.iv_up_right);

        edtLoginId = findViewById(R.id.edt_phone);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);

        //TODO--DELETE
        edtLoginId.setText("18106956212");
        edtPassword.setText("123456");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        tvModify.setOnClickListener(this);
        ivUpLeft.setOnClickListener(this);
        ivUpRight.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_modify_pwd:
                final MainActivity mainActivity1 = (MainActivity) getActivity();
                mainActivity1.setId(1);
                mainActivity1.setFragment2Fragment(new MainActivity.Fragment2Fragment() {
                    @Override
                    public void gotoFragment(VerticalViewPager viewPager) {
                        viewPager.setCurrentItem(2);
                    }
                });
                mainActivity1.forSkip();
                break;
            case R.id.iv_up_left:
                final MainActivity mainActivity2 = (MainActivity) getActivity();
                mainActivity2.setId(0);
                mainActivity2.setFragment2Fragment(new MainActivity.Fragment2Fragment() {
                    @Override
                    public void gotoFragment(VerticalViewPager viewPager) {
                        viewPager.setCurrentItem(1);
                    }
                });
                mainActivity2.forSkip();
                break;
            case R.id.iv_up_right:
                final MainActivity mainActivity3 = (MainActivity) getActivity();
                mainActivity3.setId(0);
                mainActivity3.setFragment2Fragment(new MainActivity.Fragment2Fragment() {
                    @Override
                    public void gotoFragment(VerticalViewPager viewPager) {
                        viewPager.setCurrentItem(1);
                    }
                });
                mainActivity3.forSkip();
                break;
            default:
                break;
        }
    }
    public void login() {
        String loginId = edtLoginId.getText().toString().trim();
        String password=edtPassword.getText().toString().trim();
        if(TextUtils.isEmpty(loginId)){
            showShortToast("请输入账号");
            return;
        }
        if(TextUtils.isEmpty(password)){
            showShortToast("请输入密码");
            return;
        }

        //仅测试用
        HttpRequest.login("mobile", edtLoginId.getText().toString(),edtPassword.getText().toString(),0, new OnHttpResponseListener() {

            @Override
            public void onHttpResponse(int requestCode, String resultJson, Exception e) {

                if(requestCode==0){
                    EntityLogin entityLogin =  JSON.parseObject(resultJson,EntityLogin.class);
                    if(entityLogin.isSuccess()){
                        showShortToast("登录成功");
                        HttpManager.getInstance().saveToken(entityLogin.getContent());//保存token信息
                        //
                        toActivity(MainTabActivity.createIntent(context));
                    }else{
                        showShortToast(entityLogin.getMessage());
                    }

                }
//
            }
        });
    }
}
