package com.qingye.wtsyou.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.SlidingPagerAdapter;
import com.qingye.wtsyou.entity.TabEntity;
import com.qingye.wtsyou.fragment.home.StarsMainCrowdFragment;
import com.qingye.wtsyou.fragment.home.StarsMainShowFragment;
import com.qingye.wtsyou.fragment.home.StarsMainSupportFragment;
import com.qingye.wtsyou.fragment.home.StarsMainVoteFragment;

import java.util.ArrayList;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class StarsMainActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivMore;
    private LinearLayout llFans,llConversation;
    private TextView tvFans,tvConversation;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"演出", "众筹", "投票", "应援"};
    private int[] mIconUnselectIds = {
            R.mipmap.home_b, R.mipmap.activity_b,
            R.mipmap.conversation_b, R.mipmap.personal_b};
    private int[] mIconSelectIds = {
            R.mipmap.home, R.mipmap.activity,
            R.mipmap.conversation, R.mipmap.personal};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private CommonTabLayout mTabLayout;
    private ViewPager mViewPager;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @param id
     * @return
     */
    public static Intent createIntent(Context context, long id) {
        return new Intent(context,StarsMainActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars_main,this);

        StarsMainShowFragment starsMainShowFragment = new StarsMainShowFragment();
        StarsMainCrowdFragment starsMainCrowdFragment = new StarsMainCrowdFragment();
        StarsMainVoteFragment starsMainVoteFragment = new StarsMainVoteFragment();
        StarsMainSupportFragment starsMainSupportFragment = new StarsMainSupportFragment();

        mFragments.add(starsMainShowFragment);
        mFragments.add(starsMainCrowdFragment);
        mFragments.add(starsMainVoteFragment);
        mFragments.add(starsMainSupportFragment);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    public void initView() {
        ivBack = findView(R.id.iv_left);
        ivMore = findView(R.id.iv_right);

        //llFans = findView(R.id.ll_fans);
        //llConversation = findView(R.id.ll_conversation);

        mTabLayout = findView(R.id.tab);
        mTabLayout.setBackgroundColor(Color.parseColor("#fafafa"));
        mTabLayout.setUnderlineColor(Color.parseColor("#00dddddd"));
        mViewPager = findView(R.id.viewPager);
        mViewPager.setAdapter(new SlidingPagerAdapter(getSupportFragmentManager(),mFragments,mTitles,context));
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

        mViewPager.setCurrentItem(0);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
        ivMore.setOnClickListener(this);

        //llFans.setOnClickListener(this);
        //llConversation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:

                break;
            /*case R.id.ll_fans:
                toActivity(SearchFansActivity.createIntent(context));
                break;
            case R.id.ll_conversation:
                toActivity(RecommendStarsConversationActivity.createIntent(context));
                break;*/
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
