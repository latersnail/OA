package com.snail.oa.entity;

import java.util.Date;

/**
 * Created by fangjiang on 2018/4/1.
 */
public class RecvDoc {
    private String id;
    private String processName;
    private String reason;
    private String userId;//流程启动人  主办人员
    private String pid;//流程定义ID
    private String draft;//草稿标志   0 正常流程 1 草稿数据  2  回收数据
    private String formId;// 表单ID
    private Date currentTime = new Date();
    private String takeActor;//当前任务承接人  当前办理人
    //请假流程业务数据
    private Date startTime;
    private Date endTime;
    private String suggestion;
    private String workflowName;//工作节点名称
    //是否是退回流程
    private  String retreat;
    private String pdi;//processDefinitionId
    private User user;//关联用户
    private String taskId;//任务ID
    private Form form;//关联表单
    private String money;//报销资金
    private String salary;//入职工资
    private String quarters;//申请岗位

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getQuarters() {
        return quarters;
    }

    public void setQuarters(String quarters) {
        this.quarters = quarters;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPdi() {
        return pdi;
    }

    public void setPdi(String pdi) {
        this.pdi = pdi;
    }

    public String getTakeActor() {
        return takeActor;
    }

    public void setTakeActor(String takeActor) {
        this.takeActor = takeActor;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getRetreat() {
        return retreat;
    }

    public void setRetreat(String retreat) {
        this.retreat = retreat;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}
