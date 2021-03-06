package com.qingye.wtsyou.adapter.search;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.EntityStars;
import com.qingye.wtsyou.view.search.SearchStarsView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class SearchStarsAdapter extends BaseAdapter<EntityStars,SearchStarsView> {

    public SearchStarsAdapter(Activity context) {
        super(context);
    }

    @Override
    public SearchStarsView createView(int position, ViewGroup parent) {
        return new SearchStarsView(context, parent);
    }

}
