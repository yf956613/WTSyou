package com.qingye.wtsyou.activity.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.home.FocusStarsAdapter;
import com.qingye.wtsyou.modle.Stars;
import com.qingye.wtsyou.view.home.FocusStarsView;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseHttpRecyclerActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class FocusStarsActivity extends BaseHttpRecyclerActivity<Stars,FocusStarsView,FocusStarsAdapter> implements View.OnClickListener,OnBottomDragListener {

    private ImageView ivLeft,ivAddStars;
    private TextView tvHead;
    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
            return new Intent(context, FocusStarsActivity.class);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
            return this; //必须return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_stars,this);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        //srlBaseHttpRecycler.autoRefresh();
        srlBaseHttpRecycler.setEnableRefresh(false);//不启用下拉刷新
        srlBaseHttpRecycler.setEnableLoadmore(false);//不启用上拉加载更多
        srlBaseHttpRecycler.setEnableHeaderTranslationContent(false);//头部
        srlBaseHttpRecycler.setEnableFooterTranslationContent(false);//尾部
    }

    @Override
    public void initView() {
        super.initView();
        ivLeft = findView(R.id.iv_left);
        ivLeft.setImageResource(R.mipmap.back_a);
        ivAddStars = findView(R.id.iv_right);
        ivAddStars.setImageResource(R.mipmap.add);
        tvHead = findViewById(R.id.tv_head_title);
        tvHead.setText("关注列表");
    }

    @Override
    public void setList(final List<Stars> list) {
        final List<Stars> templist = new ArrayList<>();
        for(int i = 1;i < 5;i ++) {
            Stars fans = new Stars();
            fans.setId(i);
            templist.add(fans);
        }
        //list.addAll(templist);
        setList(new AdapterCallBack<FocusStarsAdapter>() {

            @Override
            public FocusStarsAdapter createAdapter() {
                return new FocusStarsAdapter(context);
            }

            @Override
            public void refreshAdapter() {
                adapter.refresh(templist);
            }
        });
    }


    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Override
    public void initData() {//必须调用
        super.initData();

    }

    @Override
    public void getListAsync(int page) {

    }

    @Override
    public List<Stars> parseArray(String json) {
        return null;
    }

    @Override
    public void initEvent() {//必须调用
        super.initEvent();
        ivLeft.setOnClickListener(this);
        ivAddStars.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
                toActivity(SelectStarsActivity.createIntent(context));
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (id > 0) {
            toActivity(FansMainActivity.createIntent(context,id));
        }
    }

    @Override
    public void onDragBottom(boolean rightToLeft) {

    }
}
