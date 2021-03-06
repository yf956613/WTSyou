package com.qingye.wtsyou.adapter.my;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.Question;
import com.qingye.wtsyou.view.my.QuestionView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class QuestionAdapter extends BaseAdapter<Question,QuestionView> {

    public QuestionAdapter(Activity context) {
        super(context);
    }

    @Override
    public QuestionView createView(int position, ViewGroup parent) {
        return new QuestionView(context, parent);
    }

}
