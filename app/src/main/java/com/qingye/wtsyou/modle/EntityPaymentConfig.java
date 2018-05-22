package com.qingye.wtsyou.modle;

import com.qingye.wtsyou.basemodel.EntityBase;

import java.io.Serializable;
import java.util.List;

/**
 * Created by iverson_573 on 2016/3/26.
 */
public class EntityPaymentConfig extends EntityBase {

    private Content content;

    public class Content implements Serializable
    {
        //支付宝支付使用
        private String orderId;
        private List<PaySet> parameters;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public List<PaySet> getParameters() {
            return parameters;
        }

        public void setParameters(List<PaySet> parameters) {
            this.parameters = parameters;
        }
    }

    public Content getContent()
    {
        return content;
    }

    public void setContent(Content content)
    {
        this.content = content;
    }
}
