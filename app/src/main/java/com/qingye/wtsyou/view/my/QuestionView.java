package com.qingye.wtsyou.view.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.model.Question;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class QuestionView extends BaseView<Question> implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvContent;
    private ImageView ivMore;
    private ImageView ivLess;

    public QuestionView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_question, parent);
    }

    @SuppressLint("InflateParams")
    @Override
    public View createView() {
        tvTitle = findView(R.id.tv_title);
        tvContent = findView(R.id.tv_content);
        ivMore = findView(R.id.iv_more,this);
        ivLess = findView(R.id.iv_less,this);

        return super.createView();
    }

    @Override
    public void bindView(Question data_){
        super.bindView(data_ != null ? data_ : new Question());
        String No = Integer.toString(position + 1) + "、";
        tvTitle.setText(No + "关于如何使用本平台的指导");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_more:
                ivMore.setVisibility(View.GONE);
                ivLess.setVisibility(View.VISIBLE);
                tvContent.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_less:
                ivMore.setVisibility(View.VISIBLE);
                ivLess.setVisibility(View.GONE);
                tvContent.setVisibility(View.GONE);
                break;
        }
    }
}
