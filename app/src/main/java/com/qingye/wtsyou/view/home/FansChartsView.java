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
import com.qingye.wtsyou.utils.DataUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by pm89 on 2018/3/6.
 */

public class FansChartsView extends BaseView<RankInfos> implements View.OnClickListener {

    private OnItemChildClickListener onItemChildClickListener;

    //定义一个监听接口，里面有方法
    public interface OnItemChildClickListener{
        void onFocus(View view, int position, TextView focus, TextView rank);
        void onCancelFocus(View view, int position, TextView focus, TextView rank);
    }

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

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
    public void bindView(RankInfos data_){
        super.bindView(data_ != null ? data_ : new RankInfos());

        //名次
        tvNo.setText(Integer.toString(data.getRanking()));
        //名字
        tvName.setText(data.getUserName());
        //贡献值
        tvValue.setText("贡献值   " + Integer.toString(data.getScore()));
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
        if (onItemChildClickListener != null) {

            switch (v.getId()) {
                case R.id.tv_focus:
                    onItemChildClickListener.onFocus(v, position, tvFocus, tvCancelFocus);
                    break;
                case R.id.tv_cancel_focus:
                    onItemChildClickListener.onCancelFocus(v, position, tvFocus, tvCancelFocus);
                    break;
            }
        }
    }
}
