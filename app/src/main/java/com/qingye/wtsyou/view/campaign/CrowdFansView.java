package com.qingye.wtsyou.view.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.CrowdFans;

import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class CrowdFansView extends BaseView<CrowdFans> implements View.OnClickListener {

    private ImageView ivImg;
    private TextView tvName;

    public CrowdFansView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_crowd_fans, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        ivImg = findView(R.id.iv_img);
        tvName = findView(R.id.tv_name);

        return super.createView();
    }

    @Override
    public void bindView(CrowdFans data_){
        super.bindView(data_ != null ? data_ : new CrowdFans());

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
        tvName.setText(data.getUserName());
    }

    @Override
    public void onClick(View v) {

    }
}
