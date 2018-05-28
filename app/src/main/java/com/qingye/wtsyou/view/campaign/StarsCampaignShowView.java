package com.qingye.wtsyou.view.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.Concert;
import com.qingye.wtsyou.model.PriceList;
import com.qingye.wtsyou.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_2;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_5;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsCampaignShowView extends BaseView<Concert> implements View.OnClickListener {

    private TextView tvTag;
    private TextView tvName;
    private ImageView ivImg;
    private TextView tvTime;
    private TextView tvAddress;
    private TextView tvMin;
    private TextView tvMax;

    private List<PriceList> priceLists = new ArrayList<>();

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

        //最小
        tvMin = findViewById(R.id.tv_min_value);
        //最大
        tvMax = findViewById(R.id.tv_max_value);

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
        tvTime.setText(DateUtil.resverDate(data.getStartTimeStr(),DATE_PATTERN_2,DATE_PATTERN_5));
        //地址
        tvAddress.setText(data.getStadiumsName());

        //排序
        priceLists = data.getPriceList();
        double[] priceList = new double[priceLists.size()];
        for (int i = 0;i < priceLists.size();i ++) {
            priceList[i] = priceLists.get(i).getPrice().doubleValue();
        }
        sortInsert(priceList);
        //最小值
        if (priceLists.size() > 1) {
            tvMin.setText(Double.toString(priceList[0]) + " - ");
            tvMax.setText(Double.toString(priceList[priceList.length - 1]));
        } else {
            tvMin.setText(Double.toString(priceList[0]));
        }
    }

    public double[] sortInsert(double[] array){
        for(int i = 1;i < array.length;i++){
            double temp = array[i];
            int j;
            for(j = i-1;j >= 0 && temp < array[j]; j--){
                array[j + 1] = array[j];
            }
            array[j + 1] = temp;
        }
        return array;
    }

    @Override
    public void onClick(View v) {

    }
}
