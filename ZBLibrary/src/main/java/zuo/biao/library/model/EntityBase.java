package zuo.biao.library.model;

import java.io.Serializable;

/**
 * Created by pm89 on 2018/4/27.
 */

public class EntityBase implements Serializable {

    private boolean success;
    private String code;
    private String message = "";

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
