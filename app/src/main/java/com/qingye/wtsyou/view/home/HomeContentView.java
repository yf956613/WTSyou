package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.campaign.CrowdDetailedActivity;
import com.qingye.wtsyou.activity.campaign.SaleDetailedActivity;
import com.qingye.wtsyou.activity.campaign.SupportDetailedActivity;
import com.qingye.wtsyou.activity.campaign.VoteDetailedActivity;
import com.qingye.wtsyou.model.EntityHomeContent;
import com.qingye.wtsyou.utils.DateUtil;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.widget.CustomDialog;

import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_2;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_6;

/**
 * Created by pm89 on 2018/3/6.
 */

public class HomeContentView extends BaseView<EntityHomeContent> implements View.OnClickListener {

    private TextView tvTag;
    private TextView tvOperate;
    private TextView tvProvince;
    private TextView tvCity;
    private TextView tvTime;
    private TextView tvContent;

    private CustomDialog progressBar;

    public HomeContentView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_home_content, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        tvTag = findView(R.id.tv_tag);
        tvOperate = findView(R.id.tv_operate,this);
        tvProvince = findView(R.id.tv_province);
        tvCity = findView(R.id.tv_city);
        tvTime = findView(R.id.tv_time);
        tvContent = findView(R.id.tv_content);

        return super.createView();
    }

    @Override
    public void bindView(EntityHomeContent data_){
        super.bindView(data_ != null ? data_ : new EntityHomeContent());

        //标签
        String tag = data.getState();
        //投票
        if (tag.equals("voting") || tag.equals("votesuccess")) {
            tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_blue));
            tvTag.setText("投票");
            tvOperate.setText("投票");
            tvOperate.setTextColor(getColor(R.color.blue_line));
            tvOperate.setBackground(getResources().getDrawable(R.drawable.corners_blue));
        }
        //众筹
        if (tag.equals("crowding") || tag.equals("crowdsuccess")) {
            tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_blue));
            tvTag.setText("众筹");
            tvOperate.setText("众筹");
            tvOperate.setTextColor(getColor(R.color.blue_line));
            tvOperate.setBackground(getResources().getDrawable(R.drawable.corners_blue));
        }
        //售票中
        if (tag.equals("saling")) {
            tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_blue));
            tvTag.setText("演唱会");
            tvOperate.setText("购票");
            tvOperate.setTextColor(getColor(R.color.blue_line));
            tvOperate.setBackground(getResources().getDrawable(R.drawable.corners_blue));
        }

        //应援中
        if (tag.equals("supporting") || tag.equals("supportsuccess")) {
            tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_red));
            tvTag.setText("应援");
            tvOperate.setText("应援");
            tvOperate.setTextColor(getColor(R.color.pink_line));
            tvOperate.setBackground(getResources().getDrawable(R.drawable.corners_red));
        }

        if (data.getAddress() != null) {
            tvProvince.setText(data.getAddress().getPcdt().getProvince() + " · ");
            tvCity.setText(data.getAddress().getPcdt().getCity());
        }

        if (data.getStartTime() != null) {
            tvTime.setText(DateUtil.resverDate(data.getStartTime(),DATE_PATTERN_2,DATE_PATTERN_6));
        }

        if (data.getActivityName() != null) {
            tvContent.setText(data.getActivityName());
        }

    }

    private void setProgressBar() {
        progressBar = new CustomDialog(context,R.style.CustomDialog);
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void progressBarDismiss() {
        if (progressBar != null) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
                progressBar.cancel();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_operate:
                String activityState = data.getState();
                String uuid = data.getActivityId();

                if (activityState.equals("voting") || activityState.equals("votesuccess")) {

                    toActivity(VoteDetailedActivity.createIntent(context, uuid));
                }
                else if (activityState.equals("crowding") || activityState.equals("crowdsuccess")) {

                    toActivity(CrowdDetailedActivity.createIntent(context, uuid));
                }
                else if (activityState.equals("saling") || activityState.equals("over")) {

                    toActivity(SaleDetailedActivity.createIntent(context,uuid));
                }
                else if (activityState.equals("supporting") || activityState.equals("supportsuccess")) {

                    toActivity(SupportDetailedActivity.createIntent(context, uuid));
                }

                break;
        }
    }
}
