package com.snail.oa.entity;

import java.io.Serializable;

/**
 * Created by fangjiang on 2018/3/26.
 */
public class Actor implements Serializable{
    private String id;
    private String actorName;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public Actor(String id, String actorName) {
        this.id = id;
        this.actorName = actorName;
    }

    public Actor() {
    }
}
