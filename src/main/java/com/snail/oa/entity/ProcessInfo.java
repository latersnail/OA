package com.snail.oa.entity;

import java.util.Date;

/**
 * Created by fangjiang on 2018/3/28.
 */
public class ProcessInfo {

    private String id;
    private String processName;
    private String formId;
    private String deploy;
    private Date   createTime;
    private String procdefId;
    private String bpmnJson;

    public String getProcdefId() {
        return procdefId;
    }

    public void setProcdefId(String procdefId) {
        this.procdefId = procdefId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getDeploy() {
        return deploy;
    }

    public void setDeploy(String deploy) {
        this.deploy = deploy;
    }

    public String getBpmnJson() {
        return bpmnJson;
    }

    public void setBpmnJson(String bpmnJson) {
        this.bpmnJson = bpmnJson;
    }

}
