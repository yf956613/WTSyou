package com.qingye.wtsyou.view.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.EntityStars;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchStarsView extends BaseView<EntityStars> implements View.OnClickListener {

    private TextView tvName;
    private ImageView ivDelete;

    public SearchStarsView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_search_stars, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        tvName = findView(R.id.stars_name);
        ivDelete = findView(R.id.iv_delete);

        return super.createView();
    }

    @Override
    public void bindView(EntityStars data_){
        super.bindView(data_ != null ? data_ : new EntityStars());

        tvName.setText(data.getName());
        ivDelete.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

    }
}
