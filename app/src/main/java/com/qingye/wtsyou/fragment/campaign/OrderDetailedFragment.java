package com.qingye.wtsyou.fragment.campaign;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.MainActivity;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainTabActivity;
import com.qingye.wtsyou.widget.VerticalViewPager;

import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailedFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {

    public OrderDetailedFragment() {
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
        setContentView(R.layout.fragment_order_detailed);
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
    public static OrderDetailedFragment createInstance() {
        return new OrderDetailedFragment();
    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
    }


    @Override
    public void onClick(View v) {
    }
}
