package com.qingye.wtsyou.view.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.ChatingRoomItem;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SelectConversationView extends BaseView<ChatingRoomItem> implements View.OnClickListener {

    private ImageView ivBackground;
    private ImageView ivHead;
    private TextView tvCreatorName;
    private TextView tvConversationName;
    private TextView tvOnLine;
    private ImageView ivSelect;

    public SelectConversationView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_select_conversation, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        ivBackground = findView(R.id.iv_conversation_cover);
        ivHead = findView(R.id.iv_personal);
        tvCreatorName = findView(R.id.tv_creator);
        tvConversationName = findView(R.id.tv_conversation_name);
        tvOnLine = findView(R.id.tv_online);
        ivSelect = findView(R.id.iv_selected);

        return super.createView();
    }

    @Override
    public void bindView(ChatingRoomItem data_){
        super.bindView(data_ != null ? data_ : new ChatingRoomItem());

        String backUrl = data.getChatingRoom().getCoverImage();
        if (backUrl != null) {
            Glide.with(context)
                    .load(backUrl)
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.TOP)))
                    .into(ivBackground);
        }

        String headUrl = data.getChatingRoom().getOwnerPhoto();
        if (headUrl != null) {
            Glide.with(context)
                    .load(headUrl)
                    .into(ivHead);
        }
        tvCreatorName.setText(data.getChatingRoom().getOwnerName());
        tvConversationName.setText(data.getChatingRoom().getName());
        tvOnLine.setText("" + data.getChatingRoom().getUserCount());

        boolean isSelected = data.getSelector();
        if (isSelected) {
            ivSelect.setVisibility(View.VISIBLE);
        } else {
            ivSelect.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
