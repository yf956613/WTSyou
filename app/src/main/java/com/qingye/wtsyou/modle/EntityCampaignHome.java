package com.qingye.wtsyou.modle;

import com.qingye.wtsyou.basemodel.EntityBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pm89 on 2018/4/27.
 */

public class EntityCampaignHome extends EntityBase {

    private Content content;
    public class Content implements Serializable
    {
        private List<Banners> banners = new ArrayList<>();
        private List<Officials> officials = new ArrayList<>();
        private List<Hots> hots = new ArrayList<>();
        private List<Embeds> embeds = new ArrayList<>();
        private List<Supports> supports = new ArrayList<>();

        public List<Banners> getBanners() {
            return banners;
        }

        public void setBanners(List<Banners> banners) {
            this.banners = banners;
        }

        public List<Officials> getOfficials() {
            return officials;
        }

        public void setOfficials(List<Officials> officials) {
            this.officials = officials;
        }

        public List<Hots> getHots() {
            return hots;
        }

        public void setHots(List<Hots> hots) {
            this.hots = hots;
        }

        public List<Embeds> getEmbeds() {
            return embeds;
        }

        public void setEmbeds(List<Embeds> embeds) {
            this.embeds = embeds;
        }

        public List<Supports> getSupports() {
            return supports;
        }

        public void setSupports(List<Supports> supports) {
            this.supports = supports;
        }
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
