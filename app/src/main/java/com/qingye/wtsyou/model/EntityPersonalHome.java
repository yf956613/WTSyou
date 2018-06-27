package com.qingye.wtsyou.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.model.EntityBase;

/**
 * Created by pm89 on 2018/4/27.
 */

public class EntityPersonalHome extends EntityBase {

    private Content content;
    public class Content implements Serializable
    {
        private Member member;
        private List<FansIdol> stars = new ArrayList<>();
        private List<ChatingRoom> chatRooms = new ArrayList<>();
        private List<Activitys> activitys = new ArrayList<>();
        private int rank;

        public Member getMember() {
            return member;
        }

        public void setMember(Member member) {
            this.member = member;
        }

        public List<FansIdol> getStars() {
            return stars;
        }

        public void setStars(List<FansIdol> stars) {
            this.stars = stars;
        }

        public List<ChatingRoom> getChatRooms() {
            return chatRooms;
        }

        public void setChatRooms(List<ChatingRoom> chatRooms) {
            this.chatRooms = chatRooms;
        }

        public List<Activitys> getActivitys() {
            return activitys;
        }

        public void setActivitys(List<Activitys> activitys) {
            this.activitys = activitys;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
