package com.qingye.wtsyou.activity.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.Ticket;
import com.qingye.wtsyou.utils.Constant;
import com.qingye.wtsyou.utils.DateUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_2;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_6;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_9;

public class TicketDetailedActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, OnBottomDragListener {

    private ImageView ivLeft;
    private TextView tvHead;
    private ImageView bgmImg;
    private ImageView ivImg;
    private TextView tvName;
    private TextView tvAddress;
    private TextView tvDate;
    private TextView tvTime;

    private Ticket ticket;

    //启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context, Ticket ticket) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TICKET, ticket);//放进数据流中
        return new Intent(context, TicketDetailedActivity.class).putExtras(bundle);
    }

    //启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detailed,this);

        intent = getIntent();
        ticket = (Ticket) intent.getSerializableExtra(Constant.TICKET);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>
    }

    @Override
    public void initView() {
        ivLeft = findView(R.id.iv_left);
        ivLeft.setImageResource(R.mipmap.back_a);
        tvHead = findView(R.id.tv_head_title);
        tvHead.setText("门票详情");
        bgmImg = findView(R.id.bgm_img);
        ivImg = findView(R.id.iv_ticket_img);
        tvName = findView(R.id.tv_ticket_name);
        tvAddress = findView(R.id.tv_address);
        tvDate = findView(R.id.tv_date);
        tvTime = findView(R.id.tv_time);
    }

    @Override
    public void initData() {
        String url = ticket.getActivitySimple().getActivityIcon();
        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .apply(bitmapTransform(new BlurTransformation(25)))
                    .into(bgmImg);
            Glide.with(context)
                    .load(url)
                    .apply(bitmapTransform(new RoundedCornersTransformation(12, 0, RoundedCornersTransformation.CornerType.TOP)))
                    .into(ivImg);
        }
        tvName.setText(ticket.getActivitySimple().getActivityName());
        /*if (ticket.getReveiveAddress() != null) {
            //省
            String province = ticket.getReveiveAddress().getPcdt().getProvince();
            //市
            String city = ticket.getReveiveAddress().getPcdt().getCity();
            //区
            String district = ticket.getReveiveAddress().getPcdt().getDistrict();
            //街道
            String township = ticket.getReveiveAddress().getPcdt().getTownship();
            //poi
            //String poiString = data.getPoi().getPoiAddress() + data.getPoi().getPoiName();
            //详细
            String detail = ticket.getReveiveAddress().getAddress();

            tvAddress.setText("寄送地址：" + province + city + district + township + detail);
        }*/
        /*//省
        String province = ticket.getActivitySimple().getAddress().getPcdt().getProvince();
        //市
        String city = ticket.getActivitySimple().getAddress().getPcdt().getCity();
        //区
        String district = ticket.getActivitySimple().getAddress().getPcdt().getDistrict();
        //街道
        String township = ticket.getActivitySimple().getAddress().getPcdt().getTownship();
        //poi
        //String poiString = data.getPoi().getPoiAddress() + data.getPoi().getPoiName();
        //详细
        String detail = ticket.getActivitySimple().getAddress().getAddress();

        tvAddress.setText("活动地址：" + province + city + district + township + detail);*/

        if (ticket.getActivitySimple().getActivityProperty().equals("crowd")) {
            tvAddress.setText(R.string.detailStadiums);
            tvDate.setText(R.string.detailTime);
        } else {
            tvAddress.setText(ticket.getActivitySimple().getStadiumsName());
            tvDate.setText(DateUtil.resverDate(ticket.getActivitySimple().getStartTimeStr(), DATE_PATTERN_2, DATE_PATTERN_6));
            tvTime.setText(DateUtil.resverDate(ticket.getActivitySimple().getStartTimeStr(), DATE_PATTERN_2, DATE_PATTERN_9));
        }

        //tvDate.setText("18/03/12");
        //tvTime.setText("20:00");
    }

    @Override
    public void initEvent() {
        ivLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }

        return super.onKeyUp(keyCode, event);
    }
}
