package com.qingye.wtsyou.view.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import com.qingye.wtsyou.R;
import com.qingye.wtsyou.adapter.search.SearchCityAdapter;
import com.qingye.wtsyou.modle.City;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseModel;
import zuo.biao.library.base.BaseView;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchCityView extends BaseView<City> implements View.OnClickListener {

    @SuppressLint("InflateParams")

    public SearchCityAdapter mAdapter;
    public TextView name;
    public TextView firstLetter;

    public SearchCityView(Activity context, ViewGroup parent) {
        super(context, R.layout.list_search_city, parent);
    }

    @Override
    public View createView() {
        name = findViewById(R.id.tv_city_name);
        firstLetter = findViewById(R.id.tv_first_letter);
        return super.createView();
    }

    @Override
    public void bindView(City data_){
        super.bindView(data_ != null ? data_ : new City());

        name.setText(data.name);

        //获得当前的字母
        String currentWord = data.pinYin.charAt(0) + "";

        if (position > 0) {
            //获得上一个字母
            //String lastWord = city.pinYin.charAt(0)+"";
            int isFirst = data.isFirst;
            if (isFirst == 0) {
                //当前的字母和上一个字母相同，那么就隐藏firstLetter
                firstLetter.setVisibility(View.GONE);
            }else {
                firstLetter.setVisibility(View.VISIBLE);
                firstLetter.setText(currentWord);
            }
        }else{
            //显示当前
            firstLetter.setVisibility(View.VISIBLE);
            firstLetter.setText(currentWord);
        }
    }

    @Override
    public void onClick(View v) {
        if (BaseModel.isCorrect(data) == false) {
            return;
        }
    }

}
