package com.qingye.wtsyou.view.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.EntityOrder;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by pm89 on 2018/3/6.
 */

public class OrderView extends BaseView<EntityOrder> implements View.OnClickListener {

    private OnItemChildClickListener onItemChildClickListener;

    //定义一个监听接口，里面有方法
    public interface OnItemChildClickListener{
        void onCancelClick(View view, int position);
        void onCallClick(View view, int position);
        void onRemindClick(View view, int position);
        void onPayClick(View view, int position);
        void onTakeClick(View view, int position);
    }

    //给监听设置一个构造函数，用于main中调用
    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    private TextView tvOrderNumber;
    private TextView tvOrderName;
    private TextView tvOrderContent;
    private TextView tvPrice;
    private TextView tvAmount;
    private TextView tvTotal;

    private ImageView ivImg;

    private TextView tvCancel;//取消
    private TextView tvCall;//电话咨询
    private TextView tvRemind;//提醒发货
    private TextView tvPay;//付款
    private TextView tvTake;//收货
    private TextView tvOther;//其他状态

    public OrderView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_order_detailed, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {

        tvOrderNumber = findView(R.id.tv_order_number);
        tvOrderName = findView(R.id.tv_order_name);
        tvOrderContent = findView(R.id.tv_order_content);
        tvPrice = findView(R.id.tv_price);
        tvAmount = findView(R.id.tv_goods_amount);
        tvTotal = findView(R.id.tv_total_price);

        ivImg = findView(R.id.iv_order_img);

        tvCancel = findView(R.id.tv_cancel,this);
        tvCall = findView(R.id.tv_query_logistics,this);
        tvRemind = findView(R.id.tv_reminding,this);
        tvPay = findView(R.id.tv_pay,this);
        tvTake = findView(R.id.tv_take,this);
        tvOther = findView(R.id.tv_other);

        return super.createView();
    }

    @Override
    public void bindView(EntityOrder data_){
        super.bindView(data_ != null ? data_ : new EntityOrder());

        tvOrderNumber.setText(data.getId());
        tvOrderName.setText(data.getActivitySimple().getActivityName());
        tvPrice.setText(Double.toString(data.getPrice()));
        tvAmount.setText("" + data.getQty());
        tvTotal.setText(Double.toString(data.getTotal()));

        //图片
        String url = data.getActivitySimple().getActivityPic();
        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                    .into(ivImg);
        }

        String state = data.getState();
        //待付款
        if (state.equals("submitted")) {
            tvCancel.setVisibility(View.VISIBLE);
            tvCall.setVisibility(View.GONE);
            tvRemind.setVisibility(View.GONE);
            tvPay.setVisibility(View.VISIBLE);
            tvTake.setVisibility(View.GONE);
            tvOther.setVisibility(View.GONE);
        }
        //待发货
        else if (state.equals("unShip")) {
            tvCancel.setVisibility(View.GONE);
            tvCall.setVisibility(View.VISIBLE);
            tvRemind.setVisibility(View.VISIBLE);
            tvPay.setVisibility(View.GONE);
            tvTake.setVisibility(View.GONE);
            tvOther.setVisibility(View.GONE);
        }
        //待收货
        else if (state.equals("shipped")) {
            tvCancel.setVisibility(View.GONE);
            tvCall.setVisibility(View.VISIBLE);
            tvRemind.setVisibility(View.VISIBLE);
            tvPay.setVisibility(View.GONE);
            tvTake.setVisibility(View.VISIBLE);
            tvOther.setVisibility(View.GONE);
        }
        //订单取消
        else if (state.equals("cancel")) {
            tvCancel.setVisibility(View.GONE);
            tvCall.setVisibility(View.GONE);
            tvRemind.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
            tvTake.setVisibility(View.GONE);
            tvOther.setVisibility(View.VISIBLE);
            tvOther.setText("订单已取消");
            tvOther.setBackground(null);
            tvOther.setTextColor(getColor(R.color.black_text2));
        }
        //订单完成
        else if (state.equals("finish")) {
            tvCancel.setVisibility(View.GONE);
            tvCall.setVisibility(View.GONE);
            tvRemind.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
            tvTake.setVisibility(View.GONE);
            tvOther.setVisibility(View.VISIBLE);
            tvOther.setText("订单完成");
            tvOther.setBackground(null);
            tvOther.setTextColor(getColor(R.color.orange_text2));
        }
    }

    @Override
    public void onClick(View v) {
        if (onItemChildClickListener != null) {

            switch (v.getId()) {
                case R.id.tv_cancel:
                    onItemChildClickListener.onCancelClick(v, position);
                    break;
                case R.id.tv_query_logistics:
                    onItemChildClickListener.onCallClick(v, position);
                    break;
                case R.id.tv_reminding:
                    onItemChildClickListener.onRemindClick(v, position);
                    break;
                case R.id.tv_pay:
                    onItemChildClickListener.onPayClick(v, position);
                    break;
                case R.id.tv_take:
                    onItemChildClickListener.onTakeClick(v, position);
                    break;
            }
        }
    }
}
