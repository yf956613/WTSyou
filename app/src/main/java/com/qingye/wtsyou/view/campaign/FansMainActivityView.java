package com.qingye.wtsyou.view.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.Activitys;
import com.qingye.wtsyou.model.Officials;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by pm89 on 2018/3/6.
 */

public class FansMainActivityView extends BaseView<Activitys> implements View.OnClickListener {

    private TextView tvTag;
    private TextView tvName;
    private ImageView ivImg;

    public FansMainActivityView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_activity_official, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        tvTag = findView(R.id.tv_tag);
        tvTag.setVisibility(View.VISIBLE);
        tvName = findView(R.id.tv_activity_official_name);
        ivImg = findView(R.id.iv_activity_official_img);

        return super.createView();
    }

    @Override
    public void bindView(Activitys data_){
        super.bindView(data_ != null ? data_ : new Activitys());

        if (data.getActivityPic() != null) {
            String url = data.getActivityPic();
            Glide.with(context)
                    .load(url)
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(ivImg);
        }

        //标签
        String tag = data.getState();
        tvTag.setText(data.getActivityStateName());
        //投票成功、众筹完成
        if (tag.equals("votesuccess") || tag.equals("crowdsuccess") || tag.equals("unaudited")
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

        tvName.setText(data.getActivityName());

    }

    @Override
    public void onClick(View v) {

    }
}
