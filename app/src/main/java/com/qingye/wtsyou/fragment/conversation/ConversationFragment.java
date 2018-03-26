package com.qingye.wtsyou.fragment.conversation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;

import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {

    private TextView tvHead;
    private ImageView ivSearch,ivCreate;

    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //类相关初始化，必须使用<<<<<<<<<<<<<<<<
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_conversation);
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
    public static ConversationFragment createInstance() {
        return new ConversationFragment();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }

    @Override
    public void initView() {
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("聊天室");
        ivSearch = findViewById(R.id.iv_left);
        ivSearch.setImageResource(R.mipmap.search_b);
        ivCreate = findViewById(R.id.iv_right);
        ivCreate.setImageResource(R.mipmap.create);

        ConversationTopFragment converesationTopFragment = new ConversationTopFragment();
        ConversationHotFragment converesationHotFragment = new ConversationHotFragment();
        //注意这里是调用getChildFragmentManager()方法
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //把碎片添加到碎片中
        transaction.replace(R.id.list_top,converesationTopFragment);
        transaction.replace(R.id.list_hot,converesationHotFragment);
        transaction.commit();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
