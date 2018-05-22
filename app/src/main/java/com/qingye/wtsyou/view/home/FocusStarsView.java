package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.FocusStars;
import com.qingye.wtsyou.modle.Stars;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class FocusStarsView extends BaseView<FocusStars> implements View.OnClickListener {

    private OnItemChildClickListener onItemChildClickListener;

    //定义一个监听接口，里面有方法
    public interface OnItemChildClickListener{
        void onHitClick(View view, int position);
    }

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    private TextView tvName;
    private ImageView ivImg;
    private TextView tvHit;

    public FocusStarsView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_focus_stars, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        tvName = findViewById(R.id.tv_name);
        ivImg = findViewById(R.id.iv_img);
        tvHit = findViewById(R.id.tv_rank, this);

        return super.createView();
    }

    @Override
    public void bindView(FocusStars data_){
        super.bindView(data_ != null ? data_ : new FocusStars());

        tvName.setText(data.getStarName());

        String url = data.getStarPhoto();
        Glide.with(context)
                .load(url)
                .into(ivImg);
    }

    @Override
    public void onClick(View v) {
        if (onItemChildClickListener != null) {

            switch (v.getId()) {
                case R.id.ll_edit:
                    onItemChildClickListener.onHitClick(v, position);
                    break;
            }
        }
    }
}
