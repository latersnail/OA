package com.snail.oa.entity;

/**
*@description 流程节点之间的连线对象
*@author  fangjiang
*@date 2018/3/29 11:36
*/

public class Line {
    private String lineId;
    private String type;
    private String from;
    private String to;
    private String name;

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Line(String lineId, String type, String from, String to, String name) {
        this.lineId = lineId;
        this.type = type;
        this.from = from;
        this.to = to;
        this.name = name;
    }
}
