package com.qingye.wtsyou.utils;

import com.qingye.wtsyou.model.Campaign;
import com.qingye.wtsyou.model.Concert;
import com.qingye.wtsyou.model.Crowd;
import com.qingye.wtsyou.model.Supports;
import com.qingye.wtsyou.model.Vote;

import java.math.BigDecimal;

/**
 * Created by pm89 on 2018/6/2.
 */

public class CampaignUtil {

    public static Vote toVote(Campaign campaign){
        Vote vote = new Vote();
        vote.setUuid(campaign.getUuid());
        vote.setCreated(campaign.getCreated());
        vote.setModified(campaign.getModified());
        vote.setCreator(campaign.getCreator());
        vote.setModifier(campaign.getModifier());
        vote.setRelevanceStar(campaign.getRelevanceStar());
        vote.setRelevanceStarName(campaign.getRelevanceStarName());
        vote.setActivityName(campaign.getActivityName());
        vote.setActivityPic(campaign.getActivityPic());
        vote.setActivityIcon(campaign.getActivityIcon());
        vote.setDescription(campaign.getDescription());
        vote.setDetail(campaign.getDetail());
        vote.setStartTime(campaign.getStartTime());
        vote.setEndTime(campaign.getEndTime());
        vote.setState(campaign.getState());
        vote.setActivityStateName(campaign.getActivityStateName());
        vote.setCreateUserId(campaign.getCreateUserId());
        vote.setActivityType(campaign.getActivityType());
        vote.setActivityProperty(campaign.getActivityProperty());
        vote.setActivityId(campaign.getActivityId());
        vote.setAddress(campaign.getAddress());
        vote.setActivityTypeName(campaign.getActivityTypeName());
        vote.setCreateUserName(campaign.getCreateUserName());
        vote.setStartTimeStr(campaign.getStartTimeStr());
        vote.setEndTimeStr(campaign.getEndTimeStr());
        vote.setChatroomNumber(campaign.getChatroomNumber());
        vote.setVotedNumber(BigDecimal.valueOf(campaign.getJoinNumber()));
        vote.setSettingGoals(BigDecimal.valueOf(campaign.getTagerNumber()));
        vote.setDeadlineStr(campaign.getDeadlineStr());

        return vote;
    }

    public static Crowd toCrowd(Campaign campaign){
        Crowd crowd = new Crowd();
        crowd.setUuid(campaign.getUuid());
        crowd.setCreated(campaign.getCreated());
        crowd.setModified(campaign.getModified());
        crowd.setCreator(campaign.getCreator());
        crowd.setModifier(campaign.getModifier());
        crowd.setRelevanceStar(campaign.getRelevanceStar());
        crowd.setRelevanceStarName(campaign.getRelevanceStarName());
        crowd.setActivityName(campaign.getActivityName());
        crowd.setActivityPic(campaign.getActivityPic());
        crowd.setActivityIcon(campaign.getActivityIcon());
        crowd.setDescription(campaign.getDescription());
        crowd.setDetail(campaign.getDetail());
        crowd.setStartTime(campaign.getStartTime());
        crowd.setEndTime(campaign.getEndTime());
        crowd.setState(campaign.getState());
        crowd.setActivityStateName(campaign.getActivityStateName());
        crowd.setCreateUserId(campaign.getCreateUserId());
        crowd.setActivityType(campaign.getActivityType());
        crowd.setActivityProperty(campaign.getActivityProperty());
        crowd.setActivityId(campaign.getActivityId());
        crowd.setAddress(campaign.getAddress());
        crowd.setActivityTypeName(campaign.getActivityTypeName());
        crowd.setCreateUserName(campaign.getCreateUserName());
        crowd.setStartTimeStr(campaign.getStartTimeStr());
        crowd.setEndTimeStr(campaign.getEndTimeStr());
        crowd.setChatroomNumber(campaign.getChatroomNumber());
        crowd.setCrowdNum(BigDecimal.valueOf(campaign.getJoinNumber()));
        crowd.setSettingGoalsNum(BigDecimal.valueOf(campaign.getTagerNumber()));
        crowd.setDeadlineStr(campaign.getDeadlineStr());

        return crowd;
    }

    public static Concert toConcert(Campaign campaign){
        Concert concert = new Concert();
        concert.setUuid(campaign.getUuid());
        concert.setCreated(campaign.getCreated());
        concert.setModified(campaign.getModified());
        concert.setCreator(campaign.getCreator());
        concert.setModifier(campaign.getModifier());
        concert.setRelevanceStar(campaign.getRelevanceStar());
        concert.setRelevanceStarName(campaign.getRelevanceStarName());
        concert.setActivityName(campaign.getActivityName());
        concert.setActivityPic(campaign.getActivityPic());
        concert.setActivityIcon(campaign.getActivityIcon());
        concert.setDescription(campaign.getDescription());
        concert.setDetail(campaign.getDetail());
        concert.setStartTime(campaign.getStartTime());
        concert.setEndTime(campaign.getEndTime());
        concert.setState(campaign.getState());
        concert.setActivityStateName(campaign.getActivityStateName());
        concert.setCreateUserId(campaign.getCreateUserId());
        concert.setActivityType(campaign.getActivityType());
        concert.setActivityProperty(campaign.getActivityProperty());
        concert.setActivityId(campaign.getActivityId());
        concert.setAddress(campaign.getAddress());
        concert.setActivityTypeName(campaign.getActivityTypeName());
        concert.setCreateUserName(campaign.getCreateUserName());
        concert.setStartTimeStr(campaign.getStartTimeStr());
        concert.setEndTimeStr(campaign.getEndTimeStr());
        concert.setChatroomNumber(campaign.getChatroomNumber());
        concert.setPriceList(campaign.getPriceList());
        concert.setDeadlineStr(campaign.getDeadlineStr());
        concert.setStadiumsName(campaign.getStadiumsName());

        return concert;
    }

    public static Supports toSupport(Campaign campaign){
        Supports supports = new Supports();
        supports.setUuid(campaign.getUuid());
        supports.setCreated(campaign.getCreated());
        supports.setModified(campaign.getModified());
        supports.setCreator(campaign.getCreator());
        supports.setModifier(campaign.getModifier());
        supports.setRelevanceStar(campaign.getRelevanceStar());
        supports.setRelevanceStarName(campaign.getRelevanceStarName());
        supports.setActivityName(campaign.getActivityName());
        supports.setActivityPic(campaign.getActivityPic());
        supports.setActivityIcon(campaign.getActivityIcon());
        supports.setDescription(campaign.getDescription());
        supports.setDetail(campaign.getDetail());
        supports.setStartTime(campaign.getStartTime());
        supports.setEndTime(campaign.getEndTime());
        supports.setState(campaign.getState());
        //supports.setActivityStateName(campaign.getActivityStateName());
        supports.setCreateUserId(campaign.getCreateUserId());
        supports.setActivityType(campaign.getActivityType());
        supports.setActivityProperty(campaign.getActivityProperty());
        supports.setActivityId(campaign.getActivityId());
        //supports.setAddress(campaign.getAddress());
        supports.setActivityTypeName(campaign.getActivityTypeName());
        //supports.setCreateUserName(campaign.getCreateUserName());
        supports.setStartTimeStr(campaign.getStartTimeStr());
        supports.setEndTimeStr(campaign.getEndTimeStr());
        supports.setChatroomNumber(campaign.getChatroomNumber());
        supports.setSupportNum(BigDecimal.valueOf(campaign.getJoinNumber()));
        supports.setSettingGoalsNum(BigDecimal.valueOf(campaign.getTagerNumber()));
        supports.setSupportPrice(campaign.getSupportPrice());
        supports.setSettingGoalsPrice(campaign.getSettingGoalsPrice());
        supports.setDeadlineStr(campaign.getDeadlineStr());

        return supports;
    }

}
