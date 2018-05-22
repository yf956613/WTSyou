package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.activity.MainActivity;
import com.qingye.wtsyou.activity.campaign.CrowdDetailedActivity;
import com.qingye.wtsyou.activity.campaign.SaleDetailedActivity;
import com.qingye.wtsyou.activity.campaign.SupportDetailedActivity;
import com.qingye.wtsyou.activity.campaign.VoteDetailedActivity;
import com.qingye.wtsyou.modle.EntityCrowdDetailed;
import com.qingye.wtsyou.modle.EntityHomeContent;
import com.qingye.wtsyou.modle.EntitySaleDetailed;
import com.qingye.wtsyou.modle.EntitySupportDetailed;
import com.qingye.wtsyou.modle.EntityVoteDetailed;
import com.qingye.wtsyou.utils.DateUtil;
import com.qingye.wtsyou.utils.HttpRequest;
import com.qingye.wtsyou.utils.NetUtil;
import com.qingye.wtsyou.widget.CustomDialog;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.StringUtil;

import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_2;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_3;

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
        tvTag = findViewById(R.id.tv_tag);
        tvOperate = findViewById(R.id.tv_operate,this);
        tvProvince = findViewById(R.id.tv_province);
        tvCity = findViewById(R.id.tv_city);
        tvTime = findViewById(R.id.tv_time);
        tvContent = findViewById(R.id.tv_content);

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
        if (tag.equals("supporting")) {
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
            tvTime.setText(DateUtil.resverDate(data.getStartTime(),DATE_PATTERN_3,DATE_PATTERN_2));
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
                //检查网络
                if (NetUtil.checkNetwork(context)) {
                    String activityState = data.getState();
                    String uuid = data.getActivityId();

                    setProgressBar();
                    progressBar.show();

                    if (activityState.equals("voting") || activityState.equals("votesuccess")) {
                        HttpRequest.getVoteDetailed(0, uuid, new OnHttpResponseListener() {
                            @Override
                            public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                                if(!StringUtil.isEmpty(resultJson)){
                                    EntityVoteDetailed entityVoteDetailed =  JSON.parseObject(resultJson,EntityVoteDetailed.class);
                                    if(entityVoteDetailed.isSuccess()){
                                        //成功//showShortToast(R.string.getSuccess);
                                        toActivity(VoteDetailedActivity.createIntent(context, entityVoteDetailed));

                                        progressBarDismiss();
                                    }else{//显示失败信息
                                        if (entityVoteDetailed.getCode().equals("401")) {
                                            showShortToast(R.string.tokenInvalid);
                                            toActivity(MainActivity.createIntent(context));
                                        } else {
                                            showShortToast(entityVoteDetailed.getMessage());
                                        }

                                        progressBarDismiss();
                                    }
                                }else{
                                    showShortToast(R.string.noReturn);

                                    progressBarDismiss();
                                }
                            }
                        });
                    }
                    else if (activityState.equals("crowding") || activityState.equals("crowdsuccess")) {
                        HttpRequest.getCrowdDetailed(0, uuid, new OnHttpResponseListener() {
                            @Override
                            public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                                if(!StringUtil.isEmpty(resultJson)){
                                    EntityCrowdDetailed entityCrowdDetailed =  JSON.parseObject(resultJson,EntityCrowdDetailed.class);
                                    if(entityCrowdDetailed.isSuccess()){
                                        //成功//showShortToast(R.string.getSuccess);
                                        toActivity(CrowdDetailedActivity.createIntent(context, entityCrowdDetailed));

                                        progressBarDismiss();
                                    }else{//显示失败信息
                                        if (entityCrowdDetailed.getCode().equals("401")) {
                                            showShortToast(R.string.tokenInvalid);
                                            toActivity(MainActivity.createIntent(context));
                                        } else {
                                            showShortToast(entityCrowdDetailed.getMessage());
                                        }
                                    }
                                }else{
                                    showShortToast(R.string.noReturn);

                                    progressBarDismiss();
                                }
                            }
                        });
                    }
                    else if (activityState.equals("saling")) {
                        HttpRequest.getSaleDetailed(0, uuid, new OnHttpResponseListener() {
                            @Override
                            public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                                if(!StringUtil.isEmpty(resultJson)){
                                    EntitySaleDetailed entitySaleDetailed =  JSON.parseObject(resultJson,EntitySaleDetailed.class);
                                    if(entitySaleDetailed.isSuccess()){
                                        //成功//showShortToast(R.string.getSuccess);
                                        toActivity(SaleDetailedActivity.createIntent(context,entitySaleDetailed));

                                        progressBarDismiss();
                                    }else{//显示失败信息
                                        if (entitySaleDetailed.getCode().equals("401")) {
                                            showShortToast(R.string.tokenInvalid);
                                            toActivity(MainActivity.createIntent(context));
                                        } else {
                                            showShortToast(entitySaleDetailed.getMessage());
                                        }

                                        progressBarDismiss();
                                    }
                                }else{
                                    showShortToast(R.string.noReturn);

                                    progressBarDismiss();
                                }
                            }
                        });
                    }
                    else if (activityState.equals("supporting") || activityState.equals("supportsuccess")) {
                        HttpRequest.getSupportDetailed(0, uuid, new OnHttpResponseListener() {
                            @Override
                            public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                                if(!StringUtil.isEmpty(resultJson)){
                                    EntitySupportDetailed entitySupportDetailed =  JSON.parseObject(resultJson,EntitySupportDetailed.class);
                                    if(entitySupportDetailed.isSuccess()){
                                        //成功//showShortToast(R.string.getSuccess);
                                        toActivity(SupportDetailedActivity.createIntent(context, entitySupportDetailed));

                                        progressBarDismiss();
                                    }else{//显示失败信息
                                        if (entitySupportDetailed.getCode().equals("401")) {
                                            showShortToast(R.string.tokenInvalid);
                                            toActivity(MainActivity.createIntent(context));
                                        } else {
                                            showShortToast(entitySupportDetailed.getMessage());
                                        }

                                        progressBarDismiss();
                                    }
                                }else{
                                    showShortToast(R.string.noReturn);

                                    progressBarDismiss();
                                }
                            }
                        });

                        progressBarDismiss();
                    }
                } else {
                    showShortToast(R.string.checkNetwork);
                }

                break;
        }
    }
}
