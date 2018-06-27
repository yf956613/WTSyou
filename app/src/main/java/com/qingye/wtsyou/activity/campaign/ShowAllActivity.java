package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.qingye.wtsyou.citypicker.bean.BaseCity;
import com.qingye.wtsyou.citypicker.ui.CityPickerActivity;
import com.qingye.wtsyou.entity.TabEntity;
import com.qingye.wtsyou.fragment.campaign.StarsCampaignCrowdFragment;
import com.qingye.wtsyou.fragment.campaign.StarsCampaignShowFragment;
import com.qingye.wtsyou.fragment.campaign.StarsCampaignVoteFragment;
import com.qingye.wtsyou.utils.BroadcastAction;
import com.qingye.wtsyou.utils.Constant;

import java.util.ArrayList;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.widget.CustomDialog;

public class ShowAllActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivBack;
    private TextView tvHead,tvRight;
    private LinearLayout llCity;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"投票", "众筹", "演唱会"};
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

    private String cityName = null;

    private LinearLayout linearLayout;

    private CustomDialog progressBar;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context,ShowAllActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastAction.ACTION_SHOWALL_REFRESH)) {
                setView();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all,this);

        progressBar = new CustomDialog(getActivity(),R.style.CustomDialog);

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(BroadcastAction.ACTION_SHOWALL_REFRESH);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        setView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    public void initView() {
        linearLayout = findView(R.id.linearlayout);

        ivBack = findView(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("全部演出");
        llCity = findView(R.id.ll_city);
        llCity.setVisibility(View.VISIBLE);
        tvRight = findView(R.id.tv_right);

        mTabLayout = findView(R.id.tab);
        mViewPager = findView(R.id.viewPager);
    }

    public void onResume() {

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver);

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
        Bundle bundle = new Bundle();
        bundle.putString(Constant.CITYNAME, cityName);

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

        StarsCampaignVoteFragment starsCampaignVoteFragment = new StarsCampaignVoteFragment();
        starsCampaignVoteFragment.setArguments(bundle);
        StarsCampaignCrowdFragment starscampaignCrowdFragment = new StarsCampaignCrowdFragment();
        starscampaignCrowdFragment.setArguments(bundle);
        StarsCampaignShowFragment starsCampaignShowFragment = new StarsCampaignShowFragment();
        starsCampaignShowFragment.setArguments(bundle);

        mFragments.add(starsCampaignVoteFragment);
        mFragments.add(starscampaignCrowdFragment);
        mFragments.add(starsCampaignShowFragment);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        if(mViewPagerAdapter != null){
            mViewPagerAdapter = null;
        }

        mViewPagerAdapter = new SlidingPagerAdapter(fm,mFragments,mTitles,context);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);//设置缓存界面个数
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

        //设置tab
        mTabLayout.setIndicatorAnimEnable(false);
        mTabLayout.setCurrentTab(0);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
        llCity.setOnClickListener(this);
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY, R.style.CustomTheme);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.ll_city:
                //toActivity(GaoDeAddressSelectActivity.createIntent(context),REQUEST_TO_SELECT_AREA);
                Intent intent = new Intent(ShowAllActivity.this, CityPickerActivity.class);
                startActivityForResult(intent,REQUEST_TO_SELECT_AREA);
                break;
            default:
                break;
        }
    }

    private static final int REQUEST_TO_SELECT_AREA = 1;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TO_SELECT_AREA:
                if (data != null) {
                    BaseCity selectedCity = (BaseCity) data.getExtras().getSerializable(Constant.SELECTED_ADDRESS);
                    tvRight.setText(selectedCity.getCityName());
                    cityName = selectedCity.getCityName();
                    setView();
                }
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
