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
import com.qingye.wtsyou.model.EntityRankInfo;

import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HomeStarsChartsPastView extends BaseView<EntityRankInfo> implements View.OnClickListener {

    private LinearLayout ll1,ll2,ll3;
    private TextView tvPeriodsZone;
    private TextView tvFirstName,tvSecondName,tvThirdName;
    private TextView tvFirstScore,tvSecondScore,tvThirdScore;
    private ImageView ivFirstImg,ivSecondImg,ivThirdImg;

    public HomeStarsChartsPastView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_past_stars_charts, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);

        //区间
        tvPeriodsZone = findViewById(R.id.tv_periods_zone);

        //名字
        tvFirstName = findViewById(R.id.tv_first_name);
        tvSecondName = findViewById(R.id.tv_second_name);
        tvThirdName = findViewById(R.id.tv_third_name);
        //积分
        tvFirstScore = findViewById(R.id.tv_first_value);
        tvSecondScore = findViewById(R.id.tv_second_value);
        tvThirdScore = findViewById(R.id.tv_third_value);
        //图片
        ivFirstImg = findViewById(R.id.iv_first_img);
        ivSecondImg = findViewById(R.id.iv_second_img);
        ivThirdImg = findViewById(R.id.iv_third_img);

        return super.createView();
    }

    @Override
    public void bindView(EntityRankInfo data_){
        super.bindView(data_ != null ? data_ : new EntityRankInfo());

        tvPeriodsZone.setText(data.getPeriodsZone());

        if (data.getRankInfos().size() > 0) {
            ll1.setVisibility(View.VISIBLE);
            if (data.getRankInfos().get(0) != null) {
                tvFirstName.setText(data.getRankInfos().get(0).getUserName());
                tvFirstScore.setText("" + data.getRankInfos().get(0).getRanking());

                //图片
                String urlFirst = data.getRankInfos().get(0).getUserPhoto();
                if (urlFirst != null) {
                    Glide.with(context)
                            .load(urlFirst)
                            .into(ivFirstImg);
                }
            }
        }

        if (data.getRankInfos().size() > 1) {
            ll2.setVisibility(View.VISIBLE);
            if (data.getRankInfos().get(1) != null) {
                tvSecondName.setText(data.getRankInfos().get(1).getUserName());
                tvSecondScore.setText("" + data.getRankInfos().get(1).getRanking());

                //图片
                String urlFirst = data.getRankInfos().get(1).getUserPhoto();
                if (urlFirst != null) {
                    Glide.with(context)
                            .load(urlFirst)
                            .into(ivSecondImg);
                }
            }
        }

        if (data.getRankInfos().size() > 2) {
            ll3.setVisibility(View.VISIBLE);
            if (data.getRankInfos().get(2) != null) {
                tvThirdName.setText(data.getRankInfos().get(2).getUserName());
                tvThirdScore.setText("" + data.getRankInfos().get(2).getRanking());

                //图片
                String urlFirst = data.getRankInfos().get(2).getUserPhoto();
                if (urlFirst != null) {
                    Glide.with(context)
                            .load(urlFirst)
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
