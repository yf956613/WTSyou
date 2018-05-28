package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.RankInfos;

import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsChartsView extends BaseView<RankInfos> implements View.OnClickListener {

    private OnItemChildClickListener onItemChildClickListener;

    //定义一个监听接口，里面有方法
    public interface OnItemChildClickListener{
        void onFocus(View view, int position, TextView focus, TextView rank);
        void onHitClick(View view, int position, TextView focus, TextView rank);
    }

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    private TextView tvNo;
    private TextView tvName;
    private TextView tvValue;
    private TextView tvFocus;
    private ImageView ivImg;

    private TextView tvRank;

    public StarsChartsView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_newest_stars_charts, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        tvNo = findViewById(R.id.tv_No);
        tvName = findViewById(R.id.tv_stars_name);
        tvValue = findViewById(R.id.tv_value);
        tvFocus = findViewById(R.id.tv_focus,this);
        ivImg = findViewById(R.id.iv_stars_img);

        tvRank = findViewById(R.id.tv_rank,this);

        return super.createView();
    }

    @Override
    public void bindView(RankInfos data_){
        super.bindView(data_ != null ? data_ : new RankInfos());

        //名次
        tvNo.setText(Integer.toString(data.getRanking()));
        //名字
        tvName.setText(data.getUserName());
        //贡献值
        tvValue.setText(Integer.toString(data.getScore()));
        Boolean isFocus = data.getFollow();
        if (isFocus) {
            tvRank.setVisibility(View.VISIBLE);
            tvFocus.setVisibility(View.GONE);
        } else {
            tvRank.setVisibility(View.GONE);
            tvFocus.setVisibility(View.VISIBLE);
        }

        //图片
        String url = data.getUserPhoto();
        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .into(ivImg);
        }

    }

    @Override
    public void onClick(View v) {
        if (onItemChildClickListener != null) {

            switch (v.getId()) {
                case R.id.tv_focus:
                    onItemChildClickListener.onFocus(v, position, tvFocus, tvRank);
                    break;
                case R.id.tv_rank:
                    onItemChildClickListener.onHitClick(v, position, tvFocus, tvRank);
                    break;
            }
        }
    }
}
