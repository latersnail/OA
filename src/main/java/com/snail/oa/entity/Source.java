package com.snail.oa.entity;

import java.io.Serializable;

/**
 * Created by fangjiang on 2018/3/26.
 */
public class Source implements Serializable {
    private String id;
    private String name;//资源名称
    private String sourceCode;//资源码
    private String sourcePath;//资源路径   这里都是类路径为根路径
    private String root;

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

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

}
