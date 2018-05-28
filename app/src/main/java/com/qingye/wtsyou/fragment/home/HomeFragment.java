package com.qingye.wtsyou.fragment.home;


import android.os.Bundle;
import android.os.Handler;
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

    private ViewPager mViewpager;
    private CircleIndicator indicator;

    private Handler handler;

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

        /*// 在UI线程中开启一个子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 在子线程中初始化一个Looper对象
                Looper.prepare();
                handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        viewPager();
                    }
                };
                // 把刚才初始化的Looper对象运行起来，循环消息队列的消息
                Looper.loop();
            }
        }).start();*/

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        /*// 1、直接new 一个线程类，传入参数实现Runnable接口的对象（new Runnable），相当于方法二
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 写子线程中的操作

                Message msg=Message.obtain();
                msg.what=1;
                msg.obj="向子线程中发送消息！";
                // 向子线程中发送消息
                handler.sendMessage(msg);
            }
        }).start();*/

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

        viewPager();
    }

    public void viewPager() {
        //滑动界面
        mViewpager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);
        mViewpager.setAdapter(new HomePagerAdapter(context.getSupportFragmentManager()));
        indicator.setViewPager(mViewpager);
        mViewpager.setOffscreenPageLimit(1);
        //从第一页开始滑
        mViewpager.setCurrentItem(0);
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
                toActivity(SelectStarsActivity.createIntent(context, 2));
                break;
            default:
                break;

        }
    }
}
