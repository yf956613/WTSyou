package com.qingye.wtsyou.fragment.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qingye.wtsyou.MainActivity;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.widget.VerticalViewPager;

import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {

    private Button btnRegister;
    private Button btnConfirm;
    private ImageView ivDownLeft,ivDownRight;

    private LinearLayout llArea;
    private LinearLayout llAgreement;

    private int Id = 0;

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
    public void initEvent() {
        btnRegister.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        ivDownLeft.setOnClickListener(this);
        ivDownRight.setOnClickListener(this);
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
}
