package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.EntityStars;
import com.qingye.wtsyou.model.FansIdol;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class FansMainIdolView extends BaseView<FansIdol> implements View.OnClickListener {

    private ImageView ivImg;
    private TextView tvName;

    public FansMainIdolView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_head, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        ivImg = findView(R.id.iv_head);
        tvName = findView(R.id.tv_name);

        return super.createView();
    }

    @Override
    public void bindView(FansIdol data_){
        super.bindView(data_ != null ? data_ : new FansIdol());

        String url = data.getPhoto();
        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .into(ivImg);
        }
        tvName.setText(data.getName());
    }

    @Override
    public void onClick(View v) {
    }
}
