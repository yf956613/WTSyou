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

import java.math.BigDecimal;

import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchFansView extends BaseView<Fans> implements View.OnClickListener {

    private ImageView ivImg;
    private TextView tvName;
    private ImageView ivLvImg;
    private TextView tvValue;
    private TextView tvFocus;
    private TextView tvCancelFocus;
    private TextView tvTalk;
    private TextView tvCancel;

    public SearchFansView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_search_fans, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        ivImg = findViewById(R.id.iv_img);
        tvName = findViewById(R.id.tv_name);
        ivLvImg = findViewById(R.id.iv_lv_img);
        tvFocus = findViewById(R.id.tv_focus);
        tvValue = findViewById(R.id.tv_value);
        tvCancelFocus = findViewById(R.id.tv_cancel_focus);
        tvTalk = findViewById(R.id.tv_talk);
        tvCancel = findViewById(R.id.tv_cancel);
        tvFocus.setVisibility(View.GONE);

        return super.createView();
    }

    @Override
    public void bindView(Fans data_) {
        super.bindView(data_ != null ? data_ : new Fans());

        //图片
        String url = data.getUserPhoto();
        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .into(ivImg);
        }

        //名字
        tvName.setText(data.getUserName());
        //贡献值
        tvValue.setText("" + data.getLoveCount());
    }

    @Override
    public void onClick(View v) {

    }

}
