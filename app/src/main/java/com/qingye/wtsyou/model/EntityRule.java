package com.qingye.wtsyou.model;

import java.io.Serializable;

import zuo.biao.library.model.EntityBase;

/**
 * Created by pm89 on 2018/6/9.
 */

public class EntityRule extends EntityBase {

    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public class Content implements Serializable {

        private String uuid;
        private String created;
        private String modified;
        private Object creator;
        private Object modifier;
        private String activityType;
        private String ruleDescription;

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

        public Object getCreator() {
            return creator;
        }

        public void setCreator(Object creator) {
            this.creator = creator;
        }

        public Object getModifier() {
            return modifier;
        }

        public void setModifier(Object modifier) {
            this.modifier = modifier;
        }

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        public String getRuleDescription() {
            return ruleDescription;
        }

        public void setRuleDescription(String ruleDescription) {
            this.ruleDescription = ruleDescription;
        }
    }
}
