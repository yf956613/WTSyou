package com.qingye.wtsyou.model;

/**
 * Created by pm89 on 2018/4/27.
 */

public class EntityStarsItem extends EntityStars{
    private EntityStars entityStars;
    private Boolean isSelector = false;

    public EntityStars getEntityStars() {
        return entityStars;
    }

    public void setEntityStars(EntityStars entityStars) {
        this.entityStars = entityStars;
    }

    public Boolean getSelector() {
        return isSelector;
    }

    public void setSelector(Boolean selector) {
        isSelector = selector;
    }
}
