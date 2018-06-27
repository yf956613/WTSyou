package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.RankInfo;

import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HomeFansChartsPastView extends BaseView<RankInfo> implements View.OnClickListener {

    private LinearLayout ll1,ll2,ll3;
    private TextView tvPeriodsZone;
    private TextView tvFirstName,tvSecondName,tvThirdName;
    private TextView tvFirstScore,tvSecondScore,tvThirdScore;
    private ImageView ivFirstImg,ivSecondImg,ivThirdImg;

    public HomeFansChartsPastView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_past_fans_charts, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        ll1 = findView(R.id.ll1);
        ll2 = findView(R.id.ll2);
        ll3 = findView(R.id.ll3);

        //区间
        tvPeriodsZone = findView(R.id.tv_periods_zone);

        //名字
        tvFirstName = findView(R.id.tv_first_name);
        tvSecondName = findView(R.id.tv_second_name);
        tvThirdName = findView(R.id.tv_third_name);
        //积分
        tvFirstScore = findView(R.id.tv_first_value);
        tvSecondScore = findView(R.id.tv_second_value);
        tvThirdScore = findView(R.id.tv_third_value);
        //图片
        ivFirstImg = findView(R.id.iv_first_img);
        ivSecondImg = findView(R.id.iv_second_img);
        ivThirdImg = findView(R.id.iv_third_img);

        return super.createView();
    }

    @Override
    public void bindView(RankInfo data_){
        super.bindView(data_ != null ? data_ : new RankInfo());

        tvPeriodsZone.setText(data.getPeriodsZone());

        int size = data.getRankInfos().size();
        if (size > 3) {
            ll1.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
        }
        else if (size < 3 && size > 1) {
            ll1.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.GONE);
        }
        else if (size < 2 && size > 0) {
            ll1.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
        }
        else if (size == 0) {
            ll1.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.GONE);
        }

        if (data.getRankInfos().size() > 0) {
            ll1.setVisibility(View.VISIBLE);
            if (data.getRankInfos().get(0) != null) {
                tvFirstName.setText(data.getRankInfos().get(0).getUserName());
                tvFirstScore.setText("" + data.getRankInfos().get(0).getScore());

                //图片
                String urlFirst = data.getRankInfos().get(0).getUserPhoto();
                if (urlFirst != null) {
                    Glide.with(context)
                            .load(urlFirst)
                            .into(ivFirstImg);
                } else {
                    int defaultHead = R.mipmap.head;
                    Glide.with(context)
                            .load(defaultHead)
                            .into(ivFirstImg);
                }
            }
        }

        if (data.getRankInfos().size() > 1) {
            ll2.setVisibility(View.VISIBLE);
            if (data.getRankInfos().get(1) != null) {
                tvSecondName.setText(data.getRankInfos().get(1).getUserName());
                tvSecondScore.setText("" + data.getRankInfos().get(1).getScore());

                //图片
                String urlSecond = data.getRankInfos().get(1).getUserPhoto();
                if (urlSecond != null) {
                    Glide.with(context)
                            .load(urlSecond)
                            .into(ivSecondImg);
                } else {
                    int defaultHead = R.mipmap.head;
                    Glide.with(context)
                            .load(defaultHead)
                            .into(ivSecondImg);
                }
            }
        }

        if (data.getRankInfos().size() > 2) {
            ll3.setVisibility(View.VISIBLE);
            if (data.getRankInfos().get(2) != null) {
                tvThirdName.setText(data.getRankInfos().get(2).getUserName());
                tvThirdScore.setText("" + data.getRankInfos().get(2).getScore());

                //图片
                String urlThird = data.getRankInfos().get(2).getUserPhoto();
                if (urlThird != null) {
                    Glide.with(context)
                            .load(urlThird)
                            .into(ivThirdImg);
                } else {
                    int defaultHead = R.mipmap.head;
                    Glide.with(context)
                            .load(defaultHead)
                            .into(ivThirdImg);
                }

            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
