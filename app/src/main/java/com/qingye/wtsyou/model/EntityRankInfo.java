package com.qingye.wtsyou.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pm89 on 2018/4/27.
 */

public class EntityRankInfo {
    private List<RankInfos> rankInfos = new ArrayList<>();
    private String periods;
    private String periodsZone;

    public List<RankInfos> getRankInfos() {
        return rankInfos;
    }

    public void setRankInfos(List<RankInfos> starsCharts) {
        this.rankInfos = rankInfos;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getPeriodsZone() {
        return periodsZone;
    }

    public void setPeriodsZone(String periodsZone) {
        this.periodsZone = periodsZone;
    }
}
