package com.qingye.wtsyou.activity.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.qingye.wtsyou.fragment.home.HomeChartsFansFragment;
import com.qingye.wtsyou.fragment.home.HomeChartsStarsFragment;
import zuo.biao.library.widget.CustomDialog;

import java.util.ArrayList;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class ChartsActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack,ivPast;
    private TextView tvHead;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"明星排行榜","粉丝贡献榜"};
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

    private LinearLayout linearLayout;
    //private VpSwipeRefreshLayout swipeRefresh;

    private CustomDialog progressBar;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context,ChartsActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        setView();
        //刷新
        //initHrvsr();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        //swipeRefresh = findView(R.id.swipe_refresh_widget);
        linearLayout = findView(R.id.linearlayout);

        ivBack = findView(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        ivPast = findView(R.id.iv_right);
        ivPast.setImageResource(R.mipmap.calendar);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("排行榜");

        mTabLayout = findView(R.id.tab);
        mTabLayout.setUnderlineColor(Color.parseColor("#00dddddd"));
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

    //刷新
    /*private void initHrvsr(){
        //设置刷新时动画的颜色，可以设置4个
        swipeRefresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefresh.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("swipeRefresh", "invoke onRefresh...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setView();
                        showShortToast(R.string.getSuccess);
                        swipeRefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });



        // 设置子视图是否允许滚动到顶部
        swipeRefresh.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                return linearLayout.getScrollY() > 0;
            }
        });
    }*/

    public void setView() {
        mTabEntities.clear();

        //移除之前的Fragment
        final FragmentManager fm = getSupportFragmentManager();
        if (mFragments.size() > 0) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.mFragments) {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            fm.executePendingTransactions();
        }

        mFragments.clear();

        HomeChartsStarsFragment homeChartsStarsFragment = new HomeChartsStarsFragment();
        HomeChartsFansFragment homeChartsFansFragment = new HomeChartsFansFragment();

        mFragments.add(homeChartsStarsFragment);
        mFragments.add(homeChartsFansFragment);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        if(mViewPagerAdapter != null){
            mViewPagerAdapter = null;
        }

        mViewPagerAdapter = new SlidingPagerAdapter(fm,mFragments,mTitles,context);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);//设置缓存界面个数
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
        ivPast.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                toActivity(PastChartsActivity.createIntent(context));
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
