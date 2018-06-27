package com.qingye.wtsyou.view.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.Fans;

import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchFansView extends BaseView<Fans> implements View.OnClickListener {

    private OnItemChildClickListener onItemChildClickListener;

    //定义一个监听接口，里面有方法
    public interface OnItemChildClickListener{
        void onFocus(View view, int position, TextView focus, TextView rank);
        void onCancelFocus(View view, int position, TextView focus, TextView rank);
    }

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    private ImageView ivImg;
    private TextView tvName;
    private ImageView ivLvImg;
    private TextView tvValue;
    private TextView tvFocus;
    private TextView tvCancelFocus;
    private TextView tvTalk;

    public SearchFansView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_search_fans, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        ivImg = findView(R.id.iv_img);
        tvName = findView(R.id.tv_name);
        ivLvImg = findView(R.id.iv_lv_img);
        tvFocus = findView(R.id.tv_focus, this);
        tvValue = findView(R.id.tv_value);
        tvCancelFocus = findView(R.id.tv_cancel_focus, this);
        tvTalk = findView(R.id.tv_talk, this);

        return super.createView();
    }

    @Override
    public void bindView(Fans data_) {
        super.bindView(data_ != null ? data_ : new Fans());

        //图片
        String url = data.getPhoto();
        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .into(ivImg);
        } else {
            int defaultHead = R.mipmap.head;
            Glide.with(context)
                    .load(defaultHead)
                    .into(ivImg);
        }

        //名字
        tvName.setText(data.getNickname());
        //贡献值
        tvValue.setText("" + data.getLoveCount());

        if (data.isFollow()) {
            tvFocus.setVisibility(View.GONE);
            tvCancelFocus.setVisibility(View.VISIBLE);
        } else {
            tvFocus.setVisibility(View.VISIBLE);
            tvCancelFocus.setVisibility(View.GONE);
        }
        int level = data.getLevel();
        if (level == 1) {
            ivLvImg.setImageResource(R.mipmap.lv1);
        }
        else if (level == 2) {
            ivLvImg.setImageResource(R.mipmap.lv2);
        }
        else if (level == 3) {
            ivLvImg.setImageResource(R.mipmap.lv3);
        }
        else if (level == 4) {
            ivLvImg.setImageResource(R.mipmap.lv4);
        }
        else if (level == 5) {
            ivLvImg.setImageResource(R.mipmap.lv5);
        }
        else if (level == 6) {
            ivLvImg.setImageResource(R.mipmap.lv6);
        }
        else if (level == 7) {
            ivLvImg.setImageResource(R.mipmap.lv7);
        }
        else if (level == 8) {
            ivLvImg.setImageResource(R.mipmap.lv8);
        }
        else if (level == 9) {
            ivLvImg.setImageResource(R.mipmap.lv9);
        }
        else if (level == 10) {
            ivLvImg.setImageResource(R.mipmap.lv10);
        }
    }

    @Override
    public void onClick(View v) {
        if (onItemChildClickListener != null) {

            switch (v.getId()) {
                case R.id.tv_focus:
                    onItemChildClickListener.onFocus(v, position, tvFocus, tvCancelFocus);
                    break;
                case R.id.tv_cancel_focus:
                    onItemChildClickListener.onCancelFocus(v, position, tvFocus, tvCancelFocus);
                    break;
            }
        }
    }

}
