package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.EntityStars;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SelectedStarsView extends BaseView<EntityStars> implements View.OnClickListener {

    private ImageView head;

    public SelectedStarsView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_selected_stars, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        head = findViewById(R.id.iv_stars_head);

        return super.createView();
    }

    @Override
    public void bindView(EntityStars data_){
        super.bindView(data_ != null ? data_ : new EntityStars());
        Glide.with(context)
                .load(data.getPhoto())
                .into(head);
    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
