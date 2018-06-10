package com.snail.oa.entity;

import org.activiti.engine.ProcessEngineConfiguration;

/**
 * Created by fangjiang on 2018/3/31.
 */
public class ProcessDefinitionVo
{
    private String id;
    private String name;
    private String description;
    private String key;
    private int version;
    private String category;
    private String deploymentId;
    private String resourceName;
    private String tenantId = ProcessEngineConfiguration.NO_TENANT_ID;
    private Integer historyLevel;
    private String diagramResourceName;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getHistoryLevel() {
        return historyLevel;
    }

    public void setHistoryLevel(Integer historyLevel) {
        this.historyLevel = historyLevel;
    }

    public String getDiagramResourceName() {
        return diagramResourceName;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public ProcessDefinitionVo(String id, String name, String description, String key, int version, String category, String deploymentId, String resourceName, String tenantId, String diagramResourceName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.key = key;
        this.version = version;
        this.category = category;
        this.deploymentId = deploymentId;
        this.resourceName = resourceName;
        this.tenantId = tenantId;
        this.diagramResourceName = diagramResourceName;
    }
}
