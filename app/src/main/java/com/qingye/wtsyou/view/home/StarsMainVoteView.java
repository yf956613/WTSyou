package com.qingye.wtsyou.view.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.Vote;
import com.qingye.wtsyou.utils.DateUtil;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.qingye.wtsyou.utils.DateUtil.DATE_PATTERN_2;
import static com.qingye.wtsyou.utils.DateUtil.DAY_TIME_MILLIS;
import static com.qingye.wtsyou.utils.DateUtil.HOUR_TIME_MILLIS;
import static com.qingye.wtsyou.utils.DateUtil.MINUTE_TIME_MILLIS;
import static com.qingye.wtsyou.utils.DateUtil.SECOND_TIME_MILLIS;

/**
 * Created by pm89 on 2018/3/6.
 */

public class StarsMainVoteView extends BaseView<Vote> implements View.OnClickListener {

    private TextView tvTag;
    private TextView tvName;
    private ImageView ivImg;
    private TextView tvJoin;
    private TextView tvAll;
    private ProgressBar joinProgressBar;//参与人数进度条
    private TextView tvTimeDown;
    private CountDownTimer countDownTimer;

    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownMap = new SparseArray<>();

    public StarsMainVoteView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_stars_main_vote, parent);
    }

    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        if (countDownMap == null) {
            return;
        }
        Log.e("TAG",  "size :  " + countDownMap.size());
        for (int i = 0,length = countDownMap.size(); i < length; i++) {
            CountDownTimer cdt = countDownMap.get(countDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }

    @SuppressLint("NewApi")
    public static void setProgressDrawable(@NonNull ProgressBar bar, @DrawableRes int resId) {
        Drawable layerDrawable = bar.getResources().getDrawable(resId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable d = getMethod("tileify", bar, new Object[] { layerDrawable, false });
            bar.setProgressDrawable(d);
        } else {
            bar.setProgressDrawableTiled(layerDrawable);
        }
    }

    private static Drawable getMethod(String methodName, Object o, Object[] paras) {
        Drawable newDrawable = null;
        try {
            Class c[] = new Class[2];
            c[0] = Drawable.class;
            c[1] = boolean.class;
            Method method = ProgressBar.class.getDeclaredMethod(methodName, c);
            method.setAccessible(true);
            newDrawable = (Drawable) method.invoke(o, paras);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return newDrawable;
    }


    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        tvName = findView(R.id.tv_campaign_name);
        tvTag = findView(R.id.tv_tag);
        ivImg = findView(R.id.iv_campaign_img);
        tvJoin = findView(R.id.tv_join);
        tvAll = findView(R.id.tv_all);
        joinProgressBar = findView(R.id.join_progressbar);
        tvTimeDown = findView(R.id.tv_campaign_end_time);

        return super.createView();
    }

    @Override
    public void bindView(Vote data_){
        super.bindView(data_ != null ? data_ : new Vote());

        joinProgressBar.setProgress(0);
        //名称
        tvName.setText(data.getActivityName());
        //图片
        String url = data.getActivityIcon();
        Glide.with(context)
                .load(url)
                .apply(bitmapTransform(new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(ivImg);

        //标签
        String tag = data.getState();
        tvTag.setText(data.getActivityStateName());
        //投票成功
        if (tag.equals("votesuccess") || tag.equals("votefail")) {
            tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_gray));
            setProgressDrawable(joinProgressBar, R.drawable.progress_horizontal3);//进度条
        }
        //投票中
        if (tag.equals("voting")) {
            tvTag.setBackground(getResources().getDrawable(R.drawable.re_corners_red));
            setProgressDrawable(joinProgressBar, R.drawable.progress_horizontal2);//进度条
        }

        if (data.getSettingGoals() != null) {
            //参与人数
            BigDecimal joinBig = data.getVotedNumber();
            double joinDou = joinBig.doubleValue();
            int joinInt = (int) joinDou;
            tvJoin.setText(Integer.toString(joinInt));
            //目标人数
            BigDecimal allBig = data.getSettingGoals();
            double allDou = allBig.doubleValue();
            int allInt = (int) allDou;
            tvAll.setText(Integer.toString(allInt));

            if (allDou > 0 ){
                //进度条
                BigDecimal progressValueBig = new BigDecimal(joinDou/allDou);
                String progressValueStr = StringUtil.getPrice(progressValueBig);
                int progressValueInt = StringUtil.stringToInt(progressValueStr);
                joinProgressBar.setProgress(progressValueInt);
            }

        }

        //将前一个缓存清除
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        //获取当前时间
        long currentTime = System.currentTimeMillis();
        if (data.getDeadlineStr() != null) {
            long endTime = DateUtil.dateToLong(DateUtil.strToDate(data.getDeadlineStr(), DATE_PATTERN_2));
            //时间之差
            long betweenTime = DateUtil.calculateTimeDifference(endTime, currentTime);
            if (betweenTime > 0) {
                //获取控件对应的倒计时控件是否存在,存在就取消,解决时间重叠问题
                countDownTimer = new CountDownTimer(betweenTime, 1000) {
                    public void onTick(long millisUntilFinished) {
                        // 天
                        long day = millisUntilFinished / DAY_TIME_MILLIS;
                        // 小时
                        long hour = millisUntilFinished % DAY_TIME_MILLIS
                                / HOUR_TIME_MILLIS;
                        // 分钟
                        long minute = millisUntilFinished % DAY_TIME_MILLIS
                                % HOUR_TIME_MILLIS / MINUTE_TIME_MILLIS;
                        // 秒
                        long second = millisUntilFinished % DAY_TIME_MILLIS
                                % HOUR_TIME_MILLIS % MINUTE_TIME_MILLIS
                                / SECOND_TIME_MILLIS;
                        tvTimeDown.setText(day + "天" + hour + "小时" + minute + "分钟"
                                + second + "秒");
                    }

                    public void onFinish() {
                        tvTimeDown.setText(0 + "天" + 0 + "小时" + 0 + "分钟"
                                + 0 + "秒");
                    }
                }.start();
                countDownMap.put(tvTimeDown.hashCode(), countDownTimer);

            } else {
                tvTimeDown.setText(0 + "天" + 0 + "小时" + 0 + "分钟"
                        + 0 + "秒");
            }
        } else {
            tvTimeDown.setText(0 + "天" + 0 + "小时" + 0 + "分钟"
                    + 0 + "秒");
        }

    }

    @Override
    public void onClick(View v) {

    }
}

