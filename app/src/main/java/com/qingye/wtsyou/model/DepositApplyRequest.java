package com.qingye.wtsyou.model;

import com.qingye.wtsyou.basemodel.IdName;

/**
 * Created by THINK on 2018/4/16.
 */

public class DepositApplyRequest {
    private double amount;
    private IdName channel;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public IdName getChannel() {
        return channel;
    }

    public void setChannel(IdName channel) {
        this.channel = channel;
    }
}
