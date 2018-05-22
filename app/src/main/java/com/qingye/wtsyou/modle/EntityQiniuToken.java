package com.qingye.wtsyou.modle;

import com.qingye.wtsyou.basemodel.EntityBase;

import java.io.Serializable;

/**
 * Created by pm89 on 2018/4/27.
 */

public class EntityQiniuToken extends EntityBase {

    private Content content;

    public class Content implements Serializable
    {
        private String uptoken;
        private String url;

        public String getUptoken() {
            return uptoken;
        }

        public void setUptoken(String uptoken) {
            this.uptoken = uptoken;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
