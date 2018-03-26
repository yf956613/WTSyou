package com.qingye.wtsyou.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.fragment.home.StarsMainCrowdFragment;
import com.qingye.wtsyou.fragment.home.StarsMainShowFragment;
import com.qingye.wtsyou.fragment.home.StarsMainSupportFragment;
import com.qingye.wtsyou.fragment.home.StarsMainVoteFragment;

import zuo.biao.library.base.BaseTabActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class StarsMainActivity extends BaseTabActivity implements View.OnClickListener, OnBottomDragListener {

    private ImageView ivBack,ivMore;

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

        //跳到指定界面
        currentPosition = 3;

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    public void initView() {
        super.initView();
        llBaseTabTabContainer.setBackgroundResource(R.color.tab_gray);
        /*//取控件llBaseTabTabContainer当前的布局参数
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)llBaseTabTabContainer.getLayoutParams();
        //控件的高强制设置
        linearParams.height = 70;
        //使设置好的布局参数应用到控件llBaseTabTabContainer
        llBaseTabTabContainer.setLayoutParams(linearParams);*/

        ivBack = findViewById(R.id.iv_left);
        ivMore = findViewById(R.id.iv_right);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Nullable
    @Override
    public String getTitleName() {
        return null;
    }

    @Nullable
    @Override
    public String getReturnName() {
        return null;
    }

    @Nullable
    @Override
    public String getForwardName() {
        return null;
    }

    @Override
    protected String[] getTabNames() {
        return new String[] {"演出","众筹","投票","应援"};
    }

    protected Bundle getArguments(Fragment fragment) {
        Bundle bundle = fragment.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        return bundle;
    }

    @Override
    protected Fragment getFragment(int position) {
        Fragment fragment = null;
        Bundle bundle = null;
        switch (position) {
            case 0:
                fragment = new StarsMainShowFragment();
                bundle = getArguments(fragment);
                bundle.putInt(StarsMainShowFragment.ARGUMENT_POSITION, position);
                break;
            case 1:
                fragment = new StarsMainCrowdFragment();
                bundle = getArguments(fragment);
                bundle.putInt(StarsMainCrowdFragment.ARGUMENT_POSITION, position);
                break;
            case 2:
                fragment = new StarsMainVoteFragment();
                bundle = getArguments(fragment);
                bundle.putInt(StarsMainVoteFragment.ARGUMENT_POSITION, position);
                break;
            case 3:
                fragment = new StarsMainSupportFragment();
                bundle = getArguments(fragment);
                bundle.putInt(StarsMainSupportFragment.ARGUMENT_POSITION, position);
                break;
        }
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
        ivMore.setOnClickListener(this);

        topTabView.setOnTabSelectedListener(this);//覆盖super.initEvent();内的相同代码
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:

                break;
            default:
                break;
        }
    }

    @Override
    public void onDragBottom(boolean rightToLeft) {
        finish();
    }

    @Override
    public void onTabSelected(TextView tvTab, int position, int id) {
        super.onTabSelected(tvTab, position, id);
    }
}
