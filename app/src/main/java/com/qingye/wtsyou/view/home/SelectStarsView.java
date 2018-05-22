package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.EntityStarsItem;
import com.qingye.wtsyou.modle.Stars;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SelectStarsView extends BaseView<EntityStarsItem> implements View.OnClickListener {

    private ImageView head;
    private TextView name;
    private RelativeLayout rlSelected;

    public SelectStarsView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_select_stars, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        head = findViewById(R.id.iv_stars_head);
        name = findViewById(R.id.tv_name);
        rlSelected = findViewById(R.id.rl_selected);

        return super.createView();
    }

    @Override
    public void bindView(EntityStarsItem data_){
        super.bindView(data_ != null ? data_ : new EntityStarsItem());
        Glide.with(context)
                .load(data.getEntityStars().getPhoto())
                .into(head);

        name.setText(data.getEntityStars().getName());

        //根据是否选择改变打钩的样式
        if (data.getSelector() == true) {
            rlSelected.setVisibility(View.VISIBLE);
            data.setIsFollow(true);
        } else {
            rlSelected.setVisibility(View.GONE);
            data.setIsFollow(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
