package com.qingye.wtsyou.fragment.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.home.ChartsActivity;
import com.qingye.wtsyou.activity.search.SelectStarsActivity;
import com.qingye.wtsyou.adapter.home.HomePagerAdapter;

import me.relex.circleindicator.CircleIndicator;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {

    private ImageView ivCharts,ivAddstars;

    private ViewPager viewpager;
    private CircleIndicator indicator;

    public HomeFragment() {
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
        setContentView(R.layout.fragment_home);
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
    public static HomeFragment createInstance() {
        return new HomeFragment();
    }

    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {

    }

    @Override
    public void initView() {
        ivCharts = findViewById(R.id.iv_left);
        ivCharts.setImageResource(R.mipmap.paihangbang);
        ivAddstars = findViewById(R.id.iv_right);
        ivAddstars.setImageResource(R.mipmap.add);

        //滑动界面
        viewpager = (ViewPager) view.findViewById(R.id.viewPager);
        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        viewpager.setAdapter(new HomePagerAdapter(context.getSupportFragmentManager()));
        indicator.setViewPager(viewpager);
        //从第一页开始滑
        viewpager.setCurrentItem(0);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        ivCharts.setOnClickListener(this);
        ivAddstars.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                toActivity(ChartsActivity.createIntent(context));
                break;
            case R.id.iv_right:
                toActivity(SelectStarsActivity.createIntent(context));
                break;
            default:
                break;

        }
    }
}
