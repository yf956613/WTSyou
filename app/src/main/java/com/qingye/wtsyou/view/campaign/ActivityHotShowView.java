package com.qingye.wtsyou.view.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.Hots;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ActivityHotShowView extends BaseView<Hots> implements View.OnClickListener {

    private ProgressBar joinProgressBar;//参与人数进度条
    private ImageView ivImg;
    private TextView tvTag;
    private TextView tvName;

    public ActivityHotShowView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_activity_show, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        joinProgressBar = findViewById(R.id.join_progressbar);
        ivImg = findViewById(R.id.iv_hot_show_img);
        tvTag = findViewById(R.id.tv_tag);
        tvName = findViewById(R.id.tv_hot_show_name);

        return super.createView();
    }

    @Override
    public void bindView(Hots data_){
        super.bindView(data_ != null ? data_ : new Hots());
        //进度条
        joinProgressBar.setProgress((0));
        //图片
        String url = data.getActivityIcon();
        Glide.with(context)
                .load(url)
                .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(ivImg);
        //标签
        String tag = data.getActivityState();
        tvTag.setText(data.getActivityStateName());
        //投票成功、众筹完成
        if (tag.equals("votesuccess") || tag.equals("crowdsuccess")
                || tag.equals("votefail") || tag.equals("crowdfail") ) {
            tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_gray));
        }
        //投票中
        if (tag.equals("voting")) {
            tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_pink));
        }
        //众筹中
        if (tag.equals("crowding")) {
            tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_blue));
        }
        //售票中
        if (tag.equals("saling")) {
            tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_red));
        }

        //活动名称
        tvName.setText(data.getActivityName());
    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
