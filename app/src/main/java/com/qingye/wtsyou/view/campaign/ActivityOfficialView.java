package com.qingye.wtsyou.view.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.modle.Campaign;
import com.qingye.wtsyou.modle.Officials;

import org.w3c.dom.Text;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ActivityOfficialView extends BaseView<Officials> implements View.OnClickListener {

    //private RelativeLayout rlTag1,rlTag2;//1、2页标志
    private TextView tvName;
    private ImageView ivImg;

    public ActivityOfficialView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_activity_official, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        //rlTag1 = findView(R.id.rl_tag1);
        //rlTag2 = findView(R.id.rl_tag2);
        tvName = findViewById(R.id.tv_activity_official_name);
        ivImg = findViewById(R.id.iv_activity_official_img);

        return super.createView();
    }

    @Override
    public void bindView(Officials data_){
        super.bindView(data_ != null ? data_ : new Officials());
        /*if(position == 1) {
            rlTag1.setVisibility(View.INVISIBLE);
            rlTag2.setVisibility(View.VISIBLE);
        } else {
            rlTag1.setVisibility(View.VISIBLE);
            rlTag2.setVisibility(View.INVISIBLE);
        }*/
        tvName.setText(data.getActivityName());

        if (data.getActivityPic() != null) {
            String url = data.getActivityPic();
            Glide.with(context)
                    .load(url)
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(ivImg);
        }

    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }
}
