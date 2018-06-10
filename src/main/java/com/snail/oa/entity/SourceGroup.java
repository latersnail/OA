package com.snail.oa.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fangjiang on 2018/3/26.
 */
public class SourceGroup implements Serializable {
    private String id;
    private String name;
    private List<Source> sourceList;

    public List<Source> getSourceList() {
        return sourceList;
    }

    public void setSourceList(List<Source> sourceList) {
        this.sourceList = sourceList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SourceGroup(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public SourceGroup(){

    }
}
