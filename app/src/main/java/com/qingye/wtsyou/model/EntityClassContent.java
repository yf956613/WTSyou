package com.qingye.wtsyou.model;

import java.io.Serializable;

import zuo.biao.library.model.EntityBase;

/**
 * Created by pm89 on 2018/4/27.
 */

public class EntityClassContent extends EntityBase {

    private Content content;

    public class Content implements Serializable {
        private boolean success;
        private String code;
        private String message;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

}
