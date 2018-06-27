package com.qingye.wtsyou.view.conversation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.ChatingRoom;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ConversationTopView extends BaseView<ChatingRoom> implements View.OnClickListener {

    private ImageView ivImg;
    private TextView tvName;
    private TextView tvOnLine;

    public ConversationTopView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_converstion_top, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        ivImg = findView(R.id.iv_img);
        tvName = findView(R.id.tv_conversation_name);
        tvOnLine = findView(R.id.tv_online);

        return super.createView();
    }

    @Override
    public void bindView(ChatingRoom data_){
        super.bindView(data_ != null ? data_ : new ChatingRoom());

        String backUrl = data.getCoverImage();
        if (backUrl != null) {
            Glide.with(context)
                    .load(backUrl)
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(ivImg);
        }
        tvName.setText(data.getName());
        tvOnLine.setText("" + data.getUserCount());
    }

    @Override
    public void onClick(View v) {

    }
}
