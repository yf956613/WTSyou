package com.qingye.wtsyou.model;

import zuo.biao.library.model.EntityBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pm89 on 2018/4/27.
 */

public class EntityHomeList extends EntityBase {

    private Content content;
    public class Content implements Serializable
    {
        private List<RecommendStars> homeList = new ArrayList<>();

        public List<RecommendStars> getHomeList() {
            return homeList;
        }

        public void setHomeList(List<RecommendStars> homeList) {
            this.homeList = homeList;
        }
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
