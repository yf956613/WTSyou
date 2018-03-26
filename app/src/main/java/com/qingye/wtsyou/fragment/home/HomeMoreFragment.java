package com.qingye.wtsyou.fragment.home;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.home.FocusStarsActivity;
import com.qingye.wtsyou.activity.home.SelectStarsActivity;

import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeMoreFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener{

    private ImageView ivFocus,ivAll;

    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**创建一个Fragment实例
     * @return
     */
    public static HomeMoreFragment createInstance() {

        return new HomeMoreFragment();
    }

    public HomeMoreFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_home_more);
        //类相关初始化，必须使用>>>>>>>>>>>>>>>>

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        return view;
    }

    @Override
    public void initView() {
        ivFocus = findView(R.id.iv_focus);
        ivAll = findView(R.id.iv_all);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initEvent() {
        ivFocus.setOnClickListener(this);
        ivAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_focus:
                toActivity(FocusStarsActivity.createIntent(context));
                break;
            case R.id.iv_all:
                toActivity(SelectStarsActivity.createIntent(context));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }
}
