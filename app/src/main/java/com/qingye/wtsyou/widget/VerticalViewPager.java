package com.qingye.wtsyou.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.qingye.wtsyou.R;

public class VerticalViewPager extends ViewPager implements ViewPager.PageTransformer {


    public VerticalViewPager(Context context) {
        super(context);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    private void initView(Context context) {
        super.setPageTransformer(true, this);
        LayoutInflater.from(context).inflate(R.layout.vertical_view, this, true);
        //不顯示超過滑動藍色
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        //限制預加載頁面
        setOffscreenPageLimit(0);
    }

    public MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();
        float swapX = (event.getY() / height) * width;
        float swapY = (event.getX() / width) * height;
        event.setLocation(swapX, swapY);
        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        swapTouchEvent(event);
        return super.onInterceptTouchEvent(swapTouchEvent(event));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        swapTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setTranslationX(page.getWidth() * -position);
        page.setTranslationY(page.getHeight() * position);
    }
}
