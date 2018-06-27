package com.qingye.wtsyou.model;

import java.io.Serializable;

/**
 * Created by pm89 on 2018/3/9.
 */

public class ImageLoopContent implements Serializable {

    private String thumb;

    /**默认构造方法，JSON等解析时必须要有
     */
    public ImageLoopContent() {
        //default
    }
    public ImageLoopContent(String thumb) {
        this();
        this.thumb = thumb;
    }


    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

}
