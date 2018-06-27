package com.qingye.wtsyou.adapter.my;

import android.app.Activity;
import android.view.ViewGroup;

import com.qingye.wtsyou.model.Ticket;
import com.qingye.wtsyou.view.my.TicketView;

import zuo.biao.library.base.BaseAdapter;

/**
 * Created by pm89 on 2018/3/6.
 */

public class TicketAdapter extends BaseAdapter<Ticket,TicketView> {

    public TicketAdapter(Activity context) {
        super(context);
    }

    @Override
    public TicketView createView(int position, ViewGroup parent) {
        return new TicketView(context, parent);
    }

}
