package com.qingye.wtsyou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.VerticalViewPagerAdapter;
import com.qingye.wtsyou.fragment.login.LoginFragment;
import com.qingye.wtsyou.fragment.login.ModifyFragment;
import com.qingye.wtsyou.fragment.login.RegisterFragment;
import com.qingye.wtsyou.widget.VerticalViewPager;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class MainActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private VerticalViewPager verticalViewPager;
    private int Id = 0;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context,MainActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main,this);

        initView();
        initData();
        initEvent();
    }

    public void initView() {
        verticalViewPager = findViewById(R.id.viewpager);
        verticalViewPager.setAdapter(new VerticalViewPagerAdapter(getSupportFragmentManager(), getFragmentList()));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    private List<Fragment> getFragmentList() {
        List<Fragment> list = new ArrayList<>();
        LoginFragment loginFragment = new LoginFragment();
        RegisterFragment registerFragment = new RegisterFragment();
        ModifyFragment modifyFragment = new ModifyFragment();

        /*//activity往fragment传递数据
        Bundle bundle = new Bundle();
        bundle.putInt("id", 0);
        registerFragment.setArguments(bundle);*/
        list.add(loginFragment);
        list.add(registerFragment);
        list.add(modifyFragment);
        return list;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onDragBottom(boolean rightToLeft) {

    }

    //给子fragment跳转接口
    public interface Fragment2Fragment{
        public void gotoFragment(VerticalViewPager viewPager);
    }

    private  Fragment2Fragment fragment2Fragment;

    public void setFragment2Fragment(Fragment2Fragment fragment2Fragment){
        this.fragment2Fragment = fragment2Fragment;
    }

    public void forSkip(){
        if(fragment2Fragment!=null){
            fragment2Fragment.gotoFragment(verticalViewPager);
        }
    }

    public void setId(int Id){
        this.Id = Id;
    }

    public int getId(){
        return Id;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
}
