package com.qingye.wtsyou.view.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.Campaign;
import com.qingye.wtsyou.modle.Concert;
import com.qingye.wtsyou.utils.DateUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_2;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_3;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsCampaignShowView extends BaseView<Concert> implements View.OnClickListener {

    private TextView tvTag;
    private TextView tvName;
    private ImageView ivImg;
    private TextView tvTime;
    private TextView tvAddress;

    public StarsCampaignShowView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_stars_campaign_show, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        tvName = findViewById(R.id.tv_campaign_name);
        tvTag = findViewById(R.id.tv_tag);
        ivImg = findViewById(R.id.iv_campaign_img);
        tvTime = findViewById(R.id.tv_show_date);
        tvAddress = findViewById(R.id.tv_show_address);

        return super.createView();
    }

    @Override
    public void bindView(Concert data_){
        super.bindView(data_ != null ? data_ : new Concert());

        //名称
        tvName.setText(data.getActivityName());
        //图片
        String url = data.getActivityIcon();
        Glide.with(context)
                .load(url)
                .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(ivImg);

        //标签
        String tag = data.getState();
        tvTag.setText(data.getActivityStateName());
        //已结束
        if (tag.equals("over")) {
            tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_gray));
        }
        //售票中
        if (tag.equals("saling")) {
            tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_red));
        }
        //时间
        tvTime.setText(DateUtil.resverDate(data.getStartTimeStr(),DATE_PATTERN_3,DATE_PATTERN_2));
        //地址
        tvAddress.setText(data.getStadiumsName());
    }

    @Override
    public void onClick(View v) {

    }
}
