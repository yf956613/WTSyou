package com.qingye.wtsyou.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pm89 on 2018/3/6.
 */

public class ChatingRoomItem extends ChatingRoom {

    private ChatingRoom chatingRoom;
    private Boolean isSelector = false;

    public ChatingRoom getChatingRoom() {
        return chatingRoom;
    }

    public void setChatingRoom(ChatingRoom chatingRoom) {
        this.chatingRoom = chatingRoom;
    }

    public Boolean getSelector() {
        return isSelector;
    }

    public void setSelector(Boolean selector) {
        isSelector = selector;
    }
}
