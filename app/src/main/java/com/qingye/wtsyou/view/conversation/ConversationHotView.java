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

public class ConversationHotView extends BaseView<ChatingRoom> implements View.OnClickListener {

    private ImageView ivBackground;
    private ImageView ivHead;
    private TextView tvCreatorName;
    private TextView tvConversationName;
    private TextView tvOnLine;

    public ConversationHotView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_conversation_hot, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        ivBackground = findView(R.id.iv_background_img);
        ivHead = findView(R.id.iv_head_img);
        tvCreatorName = findView(R.id.tv_creator);
        tvConversationName = findView(R.id.tv_conversation_name);
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
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.TOP)))
                    .into(ivBackground);
        }
        String headUrl = data.getOwnerPhoto();
        if (headUrl != null) {
            Glide.with(context)
                    .load(headUrl)
                    .into(ivHead);
        }
        tvCreatorName.setText(data.getOwnerName());
        tvConversationName.setText(data.getName());
        tvOnLine.setText("" + data.getUserCount());
    }

    @Override
    public void onClick(View v) {

    }
}
