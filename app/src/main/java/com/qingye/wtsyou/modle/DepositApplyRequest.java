package com.qingye.wtsyou.modle;

import com.qingye.wtsyou.basemodel.IdName;

import java.math.BigDecimal;

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
