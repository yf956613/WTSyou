package com.qingye.wtsyou.activity.campaign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.search.SearchAreaActivity;
import com.qingye.wtsyou.fragment.activity.StarsCampaignCrowdFragment;
import com.qingye.wtsyou.fragment.activity.StarsCampaignShowFragment;
import com.qingye.wtsyou.fragment.activity.StarsCampaignVoteFragment;
import com.qingye.wtsyou.fragment.home.StarsMainCrowdFragment;
import com.qingye.wtsyou.fragment.home.StarsMainShowFragment;
import com.qingye.wtsyou.fragment.home.StarsMainSupportFragment;
import com.qingye.wtsyou.fragment.home.StarsMainVoteFragment;
import com.qingye.wtsyou.modle.City;
import com.qingye.wtsyou.utils.Constant;

import java.io.Serializable;

import zuo.biao.library.base.BaseTabActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class ShowAllActivity extends BaseTabActivity implements View.OnClickListener, OnBottomDragListener {

    private ImageView ivBack;
    private TextView tvHead,tvRight;
    private LinearLayout llCity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all,this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    public void initView() {
        super.initView();

        ivBack = findViewById(R.id.iv_left);
        ivBack.setImageResource(R.mipmap.back_a);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("全部演出");
        llCity = findViewById(R.id.ll_city);
        llCity.setVisibility(View.VISIBLE);
        tvRight = findViewById(R.id.tv_right);
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
        return new String[] {"投票","众筹","演唱会"};
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
                fragment = new StarsCampaignVoteFragment();
                bundle = getArguments(fragment);
                bundle.putInt(StarsCampaignVoteFragment.ARGUMENT_POSITION, position);
                break;
            case 1:
                fragment = new StarsCampaignCrowdFragment();
                bundle = getArguments(fragment);
                bundle.putInt(StarsCampaignCrowdFragment.ARGUMENT_POSITION, position);
                break;
            case 2:
                fragment = new StarsCampaignShowFragment();
                bundle = getArguments(fragment);
                bundle.putInt(StarsCampaignShowFragment.ARGUMENT_POSITION, position);
                break;
        }
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
        llCity.setOnClickListener(this);

        topTabView.setOnTabSelectedListener(this);//覆盖super.initEvent();内的相同代码
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.ll_city:
                toActivity(SearchAreaActivity.createIntent(context),REQUEST_TO_SELECT_AREA);
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
                    City selectedCity = (City) data.getExtras().getSerializable(Constant.SELECTED_CITY);
                    tvRight.setText(selectedCity.name);
                }
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
