package com.qingye.wtsyou.view.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.Ticket;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by pm89 on 2018/3/6.
 */

public class TicketView extends BaseView<Ticket> implements View.OnClickListener {

    private ImageView ivImg;
    private TextView tvName;
    private TextView tvAddress;
    private TextView tvTime;

    public TicketView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_ticket, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        ivImg = findView(R.id.iv_ticket_img);
        tvName = findView(R.id.tv_ticket_name);
        tvAddress = findView(R.id.tv_ticket_address);
        tvTime = findView(R.id.tv_ticket_time);

        return super.createView();
    }

    @Override
    public void bindView(Ticket data_){
        super.bindView(data_ != null ? data_ : new Ticket());

        String url = data.getActivitySimple().getActivityIcon();
        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(ivImg);
        }
        tvName.setText(data.getActivitySimple().getActivityName());
        if (data.getActivitySimple().getActivityProperty().equals("crowd")) {
            tvAddress.setText(R.string.detailStadiums);
            tvTime.setText(R.string.detailTime);
        } else {
            tvAddress.setText(data.getActivitySimple().getStadiumsName());
            tvTime.setText(data.getActivitySimple().getStartTimeStr());
        }
    }

    @Override
    public void onClick(View v) {

    }
}
