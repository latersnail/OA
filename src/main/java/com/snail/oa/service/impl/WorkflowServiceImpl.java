package com.snail.oa.service.impl;

import com.snail.oa.service.IWorkflowService;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * Created by fangjiang on 2018/4/21.
 */

@Service
public class WorkflowServiceImpl implements IWorkflowService{
    @Autowired
    private RepositoryService repositoryService;


    public InputStream getProcessImage(String processDefinitionId) {
        String  deploymentId =
          repositoryService.getProcessDefinition(processDefinitionId).getDeploymentId();
        List<String> names = repositoryService.getDeploymentResourceNames(deploymentId);
        for(String name : names){
            if(name.contains("png")){
               return repositoryService.getResourceAsStream(deploymentId,name);
            }
        }
        return null;
    }

    public InputStream getProcessXml(String processDefinitionId) {
        String  deploymentId =
                repositoryService.getProcessDefinition(processDefinitionId).getDeploymentId();
        List<String> names = repositoryService.getDeploymentResourceNames(deploymentId);
        for(String name : names){
            if(name.contains("bpmn")){
                return repositoryService.getResourceAsStream(deploymentId,name);
            }
        }
        return null;
    }
}
