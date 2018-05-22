package com.qingye.wtsyou.modle;

import com.qingye.wtsyou.basemodel.EntityBase;
import com.qingye.wtsyou.basemodel.POI;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by pm89 on 2018/3/6.
 */

public class EntitySaleDetailed extends EntityBase {

    private Content content;

    public class Content implements Serializable {
        private String uuid;
        private String created;
        private String modified;
        private String creator;
        private String modifier;
        private String relevanceStar;
        private String relevanceStarName;
        private String activityName;
        private String activityPic;
        private String activityIcon;
        private String description;
        private String detail;
        private Date startTime;
        private Date endTime;
        private String state;
        private String activityStateName;
        private String createUserId;
        private String activityType;
        private String activityProperty;
        private String activityId;
        private POI address;
        private String activityTypeName;
        private String stadiumsId;
        private String stadiumsName;
        private String createUserName;
        private String startTimeStr;
        private String endTimeStr;
        private int chatroomNumber;
        private int ticketSurplus;
        private int ticketNumber;
        private Date deadline;
        private String deadlineStr;
        private List<PriceList> priceList;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getModified() {
            return modified;
        }

        public void setModified(String modified) {
            this.modified = modified;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getModifier() {
            return modifier;
        }

        public void setModifier(String modifier) {
            this.modifier = modifier;
        }

        public String getRelevanceStar() {
            return relevanceStar;
        }

        public void setRelevanceStar(String relevanceStar) {
            this.relevanceStar = relevanceStar;
        }

        public String getRelevanceStarName() {
            return relevanceStarName;
        }

        public void setRelevanceStarName(String relevanceStarName) {
            this.relevanceStarName = relevanceStarName;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public String getActivityPic() {
            return activityPic;
        }

        public void setActivityPic(String activityPic) {
            this.activityPic = activityPic;
        }

        public String getActivityIcon() {
            return activityIcon;
        }

        public void setActivityIcon(String activityIcon) {
            this.activityIcon = activityIcon;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public Date getStartTime() {
            return startTime;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getActivityStateName() {
            return activityStateName;
        }

        public void setActivityStateName(String activityStateName) {
            this.activityStateName = activityStateName;
        }

        public String getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(String createUserId) {
            this.createUserId = createUserId;
        }

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        public String getActivityProperty() {
            return activityProperty;
        }

        public void setActivityProperty(String activityProperty) {
            this.activityProperty = activityProperty;
        }

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

        public POI getAddress() {
            return address;
        }

        public void setAddress(POI address) {
            this.address = address;
        }

        public String getActivityTypeName() {
            return activityTypeName;
        }

        public void setActivityTypeName(String activityTypeName) {
            this.activityTypeName = activityTypeName;
        }

        public String getStadiumsId() {
            return stadiumsId;
        }

        public void setStadiumsId(String stadiumsId) {
            this.stadiumsId = stadiumsId;
        }

        public String getStadiumsName() {
            return stadiumsName;
        }

        public void setStadiumsName(String stadiumsName) {
            this.stadiumsName = stadiumsName;
        }

        public String getCreateUserName() {
            return createUserName;
        }

        public void setCreateUserName(String createUserName) {
            this.createUserName = createUserName;
        }

        public String getStartTimeStr() {
            return startTimeStr;
        }

        public void setStartTimeStr(String startTimeStr) {
            this.startTimeStr = startTimeStr;
        }

        public String getEndTimeStr() {
            return endTimeStr;
        }

        public void setEndTimeStr(String endTimeStr) {
            this.endTimeStr = endTimeStr;
        }

        public int getChatroomNumber() {
            return chatroomNumber;
        }

        public void setChatroomNumber(int chatroomNumber) {
            this.chatroomNumber = chatroomNumber;
        }

        public int getTicketSurplus() {
            return ticketSurplus;
        }

        public void setTicketSurplus(int ticketSurplus) {
            this.ticketSurplus = ticketSurplus;
        }

        public int getTicketNumber() {
            return ticketNumber;
        }

        public void setTicketNumber(int ticketNumber) {
            this.ticketNumber = ticketNumber;
        }

        public Date getDeadline() {
            return deadline;
        }

        public void setDeadline(Date deadline) {
            this.deadline = deadline;
        }

        public String getDeadlineStr() {
            return deadlineStr;
        }

        public void setDeadlineStr(String deadlineStr) {
            this.deadlineStr = deadlineStr;
        }

        public List<PriceList> getPriceList() {
            return priceList;
        }

        public void setPriceList(List<PriceList> priceList) {
            this.priceList = priceList;
        }
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    @Override
    protected boolean isCorrect() {
        return true;
    }
}
