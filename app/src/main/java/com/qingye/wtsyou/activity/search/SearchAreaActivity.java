package com.qingye.wtsyou.activity.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.search.SearchCityAdapter;
import com.qingye.wtsyou.modle.City;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.widget.QuickIndexBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class SearchAreaActivity extends BaseHttpListActivity<City,SearchCityAdapter> implements View.OnClickListener,OnBottomDragListener {

    private ImageView ivClose;
    private TextView tvHead;

    private QuickIndexBar mQuickIndexBar;
    private TextView mLetter;

    private List<City> cities = new ArrayList<>();


    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, SearchAreaActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_area,this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //lvBaseList.onRefresh();
        lvBaseList.setPullLoadEnable(false);//下拉不加载更多
        lvBaseList.setPullRefreshEnable(false);//下拉不刷新

    }

    @Override
    public void initView() {
        super.initView();
        ivClose = findViewById(R.id.iv_left);
        ivClose.setImageResource(R.mipmap.close);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("城市选择");

        mQuickIndexBar = findViewById(R.id.quickIndexBar);
        mLetter = findViewById(R.id.tv_letter);//中间显示字母框框

        mQuickIndexBar.setOnTouchLetterListener(new QuickIndexBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter) {
                //根据当前触摸的字母，去列表中找到那个item的首字母和letter相同的，然后放置屏幕顶端
                for (int i = 0; i < cities.size(); i++) {
                    //得到当前触摸的字母
                    String firstLetter = cities.get(i).pinYin.charAt(0) + "";
                    if (firstLetter.equals(letter)){
                        //说明首字母相同，那么就放置到屏幕的顶端
                        lvBaseList.setSelection(i + 1);
                        //只需要找到第一个就好了
                        break;
                    }
                }

                //显示当前字母在框框中
                showCurrentLetter(letter);


            }
        });


        //使用缩放动画让框框消失
        ViewHelper.setScaleX(mLetter,0f);
        ViewHelper.setScaleY(mLetter,0f);
    }

    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void initEvent() {
        super.initEvent();
        ivClose.setOnClickListener(this);
    }

    @Override
    public void setList(List<City> list) {

        //准备数据
        fillList();
        //对数据进行排序
        Collections.sort(cities);

        for(int i = 0 ;i < cities.size();i ++) {
            if (i == 0) {
                cities.get(i).setIsFirst(1);
            } else {
                if (cities.get(i).pinYin.charAt(0) != cities.get(i - 1).pinYin.charAt(0)) {
                    cities.get(i).setIsFirst(1);
                }
            }
        }

        setList(new AdapterCallBack<SearchCityAdapter>() {

            @Override
            public SearchCityAdapter createAdapter() {
                return new SearchCityAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(cities);
            }
        });
    }

    @Override
    public void getListAsync(int page) {

    }

    @Override
    public List<City> parseArray(String json) {
        return null;
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
    public void onDragBottom(boolean rightToLeft) {

    }

    //使用handler完成过一段时间做某事，在1秒之后让框框消失
    private Handler mHandler = new Handler();

    private void showCurrentLetter(String letter) {
        //显示中间的框框
//        mLetter.setVisibility(View.VISIBLE);
        //使用缩放动画显示框框
        ViewPropertyAnimator.animate(mLetter)
                .scaleX(1.0f)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator())//弹性差值器
                .start();
        ViewPropertyAnimator.animate(mLetter)
                .scaleY(1.0f)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator())//弹性差值器
                .start();

        mLetter.setText(letter);

        //开始之前先清除一下
        mHandler.removeCallbacksAndMessages(null);

        //设置框框在1秒之后消失
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //这里面是在主线程中运行的
//                mLetter.setVisibility(View.GONE);
                //隐藏也是用属性动画
                ViewPropertyAnimator.animate(mLetter)
                        .scaleX(0)
                        .setDuration(300)
                        .setInterpolator(new OvershootInterpolator())
                        .start();
                ViewPropertyAnimator.animate(mLetter)
                        .scaleY(0)
                        .setDuration(300)
                        .setInterpolator(new OvershootInterpolator())
                        .start();
            }
        },1000);

    }

    private void fillList() {
        // 虚拟数据
        cities.add(new City("伊尼戈-洛佩斯",1,0));
        cities.add(new City("阿梅奥比",2,0));
        cities.add(new City("孙兴民",3,0));
        cities.add(new City("伊布拉希莫维奇",4,0));
        cities.add(new City("伊班",5,0));
        cities.add(new City("梅西",6,0));
        cities.add(new City("姆希塔扬",7,0));
        cities.add(new City("伊沃",8,0));
        cities.add(new City("阿圭罗",9,0));
        cities.add(new City("奥古斯托",10,0));
        cities.add(new City("鲁尼",11,0));
        cities.add(new City("科斯切尔尼",12,0));
        cities.add(new City("张伯伦",13,0));
        cities.add(new City("苏亚雷斯",14,0));
        cities.add(new City("亚亚-图雷",15,0));
        cities.add(new City("周杰伦",16,0));
        cities.add(new City("陈坤",17,0));
        cities.add(new City("王寿挺",18,0));
        cities.add(new City("李刚",19,0));
        cities.add(new City("哈维",20,0));
        cities.add(new City("林俊杰",21,0));
        cities.add(new City("阿里",22,0));
        cities.add(new City("图兰",23,0));
        cities.add(new City("赵四",24,0));
        cities.add(new City("尼古拉斯-赵四",25,0));
        cities.add(new City("赵子龙",26,0));
        cities.add(new City("格列兹曼",27,0));
        cities.add(new City("诺伊尔",28,0));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        City selectedCity = adapter.getItem(position);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.SELECTED_CITY,selectedCity);//放进数据流中

        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
