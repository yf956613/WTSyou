package com.qingye.wtsyou.view.campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.ChatingRoom;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by pm89 on 2018/3/6.
 */

public class AssociateConversationView extends BaseView<ChatingRoom> implements View.OnClickListener {

    private ImageView ivBackground;

    public AssociateConversationView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_associate_conversation, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        ivBackground = findView(R.id.iv_select_converstion);

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
                    .into(ivBackground);
        }
    }

    @Override
    public void onClick(View v) {

    }

}
