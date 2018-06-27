package com.qingye.wtsyou.adapter.home;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.FocusStars;
import com.qingye.wtsyou.view.home.FocusStarsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class FocusStarsAdapter extends BaseAdapter<FocusStars,FocusStarsView> {

    private FocusStarsView.OnItemChildClickListener onItemChildClickListener;

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(FocusStarsView.OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public FocusStarsAdapter(Activity context) {
        super(context);
    }

    @Override
    public FocusStarsView createView(int position, ViewGroup parent) {

        FocusStarsView focusStarsView = new FocusStarsView(context, parent);
        focusStarsView.setOnItemChildClickListener(onItemChildClickListener);

        return focusStarsView;
    }
}
