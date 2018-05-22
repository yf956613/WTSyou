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

public class EntitySignRecord extends EntityBase {

    private Content content;

    public class Content implements Serializable {
        public String uuid;
        public String created;
        public String modified;
        public String creator;
        public String modifier;
        public String userId;
        public int signCount;
        public int continuousCount;
        public String lastTime;

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getSignCount() {
            return signCount;
        }

        public void setSignCount(int signCount) {
            this.signCount = signCount;
        }

        public int getContinuousCount() {
            return continuousCount;
        }

        public void setContinuousCount(int continuousCount) {
            this.continuousCount = continuousCount;
        }

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String lastTime) {
            this.lastTime = lastTime;
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
