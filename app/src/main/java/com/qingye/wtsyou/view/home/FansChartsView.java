package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.FansCharts;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by pm89 on 2018/3/6.
 */

public class FansChartsView extends BaseView<FansCharts> implements View.OnClickListener {

    private TextView tvNo;
    private TextView tvName;
    private TextView tvValue;
    private TextView tvFocus;
    private TextView tvCancelFocus;
    private ImageView ivImg;

    public FansChartsView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_newest_fans_charts, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        tvNo = findViewById(R.id.tv_No);
        tvName = findViewById(R.id.tv_stars_name);
        tvValue = findViewById(R.id.tv_value);
        tvFocus = findViewById(R.id.tv_focus,this);
        tvCancelFocus = findViewById(R.id.tv_cancel_focus,this);
        ivImg = findViewById(R.id.iv_stars_img);

        return super.createView();
    }

    @Override
    public void bindView(FansCharts data_){
        super.bindView(data_ != null ? data_ : new FansCharts());

        //名次
        tvNo.setText(Integer.toString(data.getRanking()));
        //名字
        tvName.setText(data.getUserName());
        //贡献值
        tvValue.setText(Integer.toString(data.getScore()));
        Boolean isFocus = data.getFollow();
        if (isFocus) {
            tvCancelFocus.setVisibility(View.VISIBLE);
            tvFocus.setVisibility(View.GONE);
        } else {
            tvCancelFocus.setVisibility(View.GONE);
            tvFocus.setVisibility(View.VISIBLE);
        }

        //图片
        String url = data.getUserPhoto();
        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(ivImg);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_focus:
                tvCancelFocus.setVisibility(View.VISIBLE);
                tvFocus.setVisibility(View.GONE);
                break;
            case R.id.tv_cancel_focus:
                tvFocus.setVisibility(View.VISIBLE);
                tvCancelFocus.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}
