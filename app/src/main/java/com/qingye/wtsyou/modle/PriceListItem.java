package com.qingye.wtsyou.modle;

/**
 * Created by pm89 on 2018/5/10.
 */

public class PriceListItem extends PriceList {
    private PriceList priceList;
    private Boolean isSelector = false;

    public PriceList getPriceList() {
        return priceList;
    }

    public void setPriceList(PriceList priceList) {
        this.priceList = priceList;
    }

    public Boolean getSelector() {
        return isSelector;
    }

    public void setSelector(Boolean selector) {
        isSelector = selector;
    }

    @Override
    protected boolean isCorrect() {
        return false;
    }
}
