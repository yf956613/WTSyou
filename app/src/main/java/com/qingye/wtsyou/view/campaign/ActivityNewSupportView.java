package com.qingye.wtsyou.view.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.Supports;

import java.math.BigDecimal;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.StringUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ActivityNewSupportView extends BaseView<Supports> implements View.OnClickListener {

    private ImageView ivImg;
    private TextView tvTag;
    private TextView tvName;
    private TextView tvEndTime;
    private ProgressBar joinProgressBar;
    private TextView tvJoin;
    private TextView tvAll;
    private LinearLayout llPeople;

    public ActivityNewSupportView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_activity_support, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        ivImg = findViewById(R.id.iv_new_support_img);
        tvTag = findViewById(R.id.tv_tag);
        tvName = findViewById(R.id.tv_support_name);
        tvEndTime = findViewById(R.id.tv_end_time);
        joinProgressBar = findViewById(R.id.join_progressbar);
        llPeople = findViewById(R.id.ll_people);
        tvJoin = findViewById(R.id.tv_join);
        tvAll = findViewById(R.id.tv_all);

        return super.createView();
    }

    @Override
    public void bindView(Supports data_){
        super.bindView(data_ != null ? data_ : new Supports());
        //图片
        String url = data.getActivityPic();
        Glide.with(context)
                .load(url)
                .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(ivImg);
        //标签
        String tag = data.getState();
        if (!tag.isEmpty()) {
            //应援中
            if (tag.equals("supporting")) {
                tvTag.setText("应援中");
                tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_pink));
            }
            //应援成功
            if (tag.equals("supportsuccess") || tag.equals("supportfail")) {
                tvTag.setText("已结束");
                tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_gray));
            }
        }

        //活动名称
        tvName.setText(data.getActivityName());
        //截止时间
        tvEndTime.setText(data.getDeadlineStr());

        if (data.getSettingGoalsPrice() != null) {
            llPeople.setVisibility(View.GONE);
            //筹集金额
            BigDecimal joinBig = data.getSupportPrice();
            double joinDou = joinBig.doubleValue();
            int joinInt = (int) joinDou;
            //目标金额
            BigDecimal allBig = data.getSettingGoalsPrice();
            double allDou = allBig.doubleValue();
            int allInt = (int) allDou;

            int progressValueInt = 0;
            if (allDou > 0) {
                //进度条
                BigDecimal progressValueBig = new BigDecimal(joinDou/allDou);
                String progressValueStr = StringUtil.getPrice(progressValueBig);
                progressValueInt = StringUtil.stringToInt(progressValueStr);
            }
            joinProgressBar.setProgress(progressValueInt);
        }

        if (data.getSettingGoalsNum() != null) {
            llPeople.setVisibility(View.VISIBLE);
            tvJoin.setVisibility(View.VISIBLE);
            tvAll.setVisibility(View.VISIBLE);
            //参与人数
            BigDecimal joinBig = data.getSupportNum();
            double joinDou = joinBig.doubleValue();
            int joinInt = (int) joinDou;
            tvJoin.setText(Integer.toString(joinInt));
            //目标人数
            BigDecimal allBig = data.getSettingGoalsNum();
            double allDou = allBig.doubleValue();
            int allInt = (int) allDou;
            tvAll.setText(Integer.toString(allInt));

            int progressValueInt = 0;
            if (allDou > 0) {
                //进度条
                BigDecimal progressValueBig = new BigDecimal(joinDou/allDou);
                String progressValueStr = StringUtil.getPrice(progressValueBig);
                progressValueInt = StringUtil.stringToInt(progressValueStr);
            }
            joinProgressBar.setProgress(progressValueInt);
        }
    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
