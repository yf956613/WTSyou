package com.qingye.wtsyou.modle;

import zuo.biao.library.base.BaseModel;

/**
 * Created by pm89 on 2018/3/9.
 */

public class ImageLoopContent extends BaseModel {

    private String thumb;

    /**默认构造方法，JSON等解析时必须要有
     */
    public ImageLoopContent() {
        //default
    }
    public ImageLoopContent(long id,String thumb) {
        this();
        this.id = id;
        this.thumb = thumb;
    }


    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    protected boolean isCorrect() {
        return true;
    }
}
