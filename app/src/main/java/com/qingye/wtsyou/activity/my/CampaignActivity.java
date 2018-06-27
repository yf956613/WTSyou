package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.SlidingPagerAdapter;
import com.qingye.wtsyou.entity.TabEntity;
import com.qingye.wtsyou.fragment.my.MyCampaignCrowdFragment;
import com.qingye.wtsyou.fragment.my.MyCampaignShowFragment;
import com.qingye.wtsyou.fragment.my.MyCampaignSupportFragment;
import com.qingye.wtsyou.fragment.my.MyCampaignVoteFragment;

import java.util.ArrayList;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.widget.CustomDialog;

public class CampaignActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack;
    private TextView tvHead;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"投票", "众筹", "演唱会", "应援"};
    private int[] mIconUnselectIds = {
            R.mipmap.home_b, R.mipmap.activity_b,
            R.mipmap.conversation_b, R.mipmap.personal_b};
    private int[] mIconSelectIds = {
            R.mipmap.home, R.mipmap.activity,
            R.mipmap.conversation, R.mipmap.personal};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private CommonTabLayout mTabLayout;
    private SlidingPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;

    private CustomDialog progressBar;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context,CampaignActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        setView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        ivBack = findView(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("我的活动");

        mTabLayout = findView(R.id.tab);
        mViewPager = findView(R.id.viewPager);
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

    public void setView() {
        mTabEntities.clear();

        //移除之前的Fragment
        final FragmentManager fm = getSupportFragmentManager();
        if (mFragments.size() > 0) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.mFragments) {
                ft.remove(f);
            }
            ft.commitAllowingStateLoss();
            ft = null;
            fm.executePendingTransactions();
        }

        mFragments.clear();

        MyCampaignVoteFragment myCampaignVoteFragment = new MyCampaignVoteFragment();
        MyCampaignCrowdFragment myCampaignCrowdFragment = new MyCampaignCrowdFragment();
        MyCampaignShowFragment myCampaignShowFragment = new MyCampaignShowFragment();
        MyCampaignSupportFragment myCampaignSupportFragment = new MyCampaignSupportFragment();

        mFragments.add(myCampaignVoteFragment);
        mFragments.add(myCampaignCrowdFragment);
        mFragments.add(myCampaignShowFragment);
        mFragments.add(myCampaignSupportFragment);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        if(mViewPagerAdapter != null){
            mViewPagerAdapter = null;
        }

        mViewPagerAdapter = new SlidingPagerAdapter(fm,mFragments,mTitles,context);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);//设置缓存界面个数
        tabLayout();
    }

    private void tabLayout() {
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.setIndicatorAnimEnable(false);
        mTabLayout.setCurrentTab(0);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
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
}
