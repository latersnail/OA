package com.snail.oa.entity;

/**
 * Created by fangjiang on 2018/4/14.
 */
public class Authority {
    private String id;
    private String actorId;
    private String sourceId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Authority(String actorId, String sourceId) {
        this.actorId = actorId;
        this.sourceId = sourceId;
    }

    public Authority(){}
}
