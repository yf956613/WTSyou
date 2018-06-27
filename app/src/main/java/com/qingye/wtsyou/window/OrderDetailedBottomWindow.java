package com.qingye.wtsyou.window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qingye.wtsyou.R;

/**
 * Created by pm89 on 2018/4/23.
 */

public class OrderDetailedBottomWindow extends PopupWindow implements View.OnClickListener {

    private TextView tvClose;
    private View mPopView;
    private OnItemClickListener mListener;

    public OrderDetailedBottomWindow(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
        setPopupWindow();
        tvClose.setOnClickListener(this);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        mPopView = inflater.inflate(R.layout.order_detailed_bottom_window, null);
        tvClose = (TextView) mPopView.findViewById(R.id.tv_close);
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);// 设置View
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口焦点
        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));// 设置背景
        this.setOutsideTouchable(true);//点击外面窗口消失
        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = mPopView.findViewById(R.id.rlDetailed).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnDetailedItemClickListener(View v);
    }

    public void setOnDetailedItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mListener != null) {
            mListener.setOnDetailedItemClickListener(v);
        }
    }

}
