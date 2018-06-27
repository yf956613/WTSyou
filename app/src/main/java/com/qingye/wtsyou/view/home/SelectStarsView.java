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
import com.qingye.wtsyou.model.EntityStars;
import com.qingye.wtsyou.model.EntityStarsItem;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SelectStarsView extends BaseView<EntityStars> implements View.OnClickListener {

    private ImageView head;
    private TextView name;
    private RelativeLayout rlSelected;

    public SelectStarsView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_select_stars, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        head = findView(R.id.iv_stars_head);
        name = findView(R.id.tv_name);
        rlSelected = findView(R.id.rl_selected);

        return super.createView();
    }

    @Override
    public void bindView(EntityStars data_){
        super.bindView(data_ != null ? data_ : new EntityStars());
        Glide.with(context)
                .load(data.getPhoto())
                .into(head);

        name.setText(data.getName());

        //根据是否选择改变打钩的样式
        if (data.getIsFollow() == true) {
            rlSelected.setVisibility(View.VISIBLE);
            data.setIsFollow(true);
        } else {
            rlSelected.setVisibility(View.GONE);
            data.setIsFollow(false);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
