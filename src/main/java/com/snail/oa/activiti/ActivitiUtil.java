package com.snail.oa.activiti;

import com.snail.oa.exception.SnailException;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

/**
*@description activiti帮助类  由IOC进行管理   取用使用注解
*@author  fangjiang
*@date 2018/4/5 20:39
*/

@Component
public class ActivitiUtil {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ProcessEngine processEngine;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
    *@description 获取当前节点的下一个节点实例
    *@param  processInstanceId 流程定义ID
    *@author  fangjiang
    *@date 2018/4/5 20:49
    */
    public FlowNode getNextNode(String processInstanceId){
        List<SequenceFlow> sequenceFlowList = getOutTransition(processInstanceId);
        if(sequenceFlowList.size()==1){
            return (FlowNode) sequenceFlowList.get(0).getTargetFlowElement();
        }
        return null;
    }

    /**
    *@description 获取当前节点的输出迁移线信息
    *@author  fangjiang
    *@date 2018/4/6 11:35
    */

    public List<SequenceFlow> getOutTransition(String processInstanceId){
        String activityId = getRunActivityId(processInstanceId);

        List<FlowNode> flowNodeList = getAllNode(processInstanceId);
        for (FlowNode flowNode:flowNodeList){
            if (activityId.equals(flowNode.getId())){
              return  flowNode.getOutgoingFlows();
          }
        }
        return null;
    }

    /**
    *@description 获取当前节点的所有输入迁移线信息
    *@author  fangjiang
    *@date 2018/4/24 13:41
    */
    public List<SequenceFlow> getInTransition(String processInstanceId){
        String activityId = getRunActivityId(processInstanceId);

        List<FlowNode> flowNodeList = getAllNode(processInstanceId);
        for (FlowNode flowNode:flowNodeList){
            if (activityId.equals(flowNode.getId())){
                return  flowNode.getIncomingFlows();
            }
        }
        return null;
    }


    /**
    *@description 获取当前流程的所有节点数据
    *@author  fangjiang
    *@date 2018/4/6 11:35
    */

    public List<FlowNode> getAllNode(String processInstanceId){
        String processDefinitionId = getProcessDefinitionId(processInstanceId);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
        List<FlowNode> flowNodeList = new ArrayList<FlowNode>();
        for(FlowElement flowElement: flowElements){
            //过滤迁移线数据 因为SequenceFlow直接继承 FlowElement
            if(flowElement instanceof FlowNode){
                FlowNode flowNode = (FlowNode) flowElement;
                flowNodeList.add(flowNode);
            }
        }
        return  flowNodeList;
    }

    /**
    *@description 获取当前正要处理的节点
    *@author  fangjiang
    *@date 2018/4/6 11:45
    */
    public String getRunActivityId(String processInstanceId){
        //根据流程定义ID获取当前任务
       // ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
        TaskEntity taskEntity =
                (TaskEntity) taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        String executionId = taskEntity.getExecutionId();
        Execution execution =
                runtimeService.createExecutionQuery().executionId(executionId).singleResult();
        String activityId = execution.getActivityId();
        return activityId;
    }

    /**
    *@description 根据流程实例获取任务实例ID
    *@author  fangjiang
    *@date 2018/4/7 16:46
    */
    public String getTaskId(String processInstanceId){
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processInstanceId(processInstanceId).singleResult();
        return  task.getId();
    }

    /**
    *@description 根据流程实例ID获取任务执行ID
    *@author  fangjiang
    *@date 2018/4/23 16:21
    */
    public String getExecuteId(String processInstanceId){
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processInstanceId(processInstanceId).singleResult();
        return task.getExecutionId();
    }


    /**
    *@description 根据流程实例获取流程定义id
    *@author  fangjiang
    *@date 2018/4/10 21:48
    */
    public String getProcessDefinitionId(String processInstanceId){
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processInstanceId(processInstanceId).singleResult();
        return  task.getProcessDefinitionId();
    }

    /**
    *@description 获取排他网关的流转下一节点信息
    *@author  fangjiang
    *@date 2018/4/23 14:29
    */

    public String findGateWayTargetNode(FlowElement flowElement,String processInstanceId) throws SnailException {
        //将flowNode强转为ExclusiveGateway
        ExclusiveGateway gateway = (ExclusiveGateway) flowElement;
        List<SequenceFlow> sequenceFlowList = gateway.getOutgoingFlows();
        String executeId = this.getExecuteId(processInstanceId);
        for(SequenceFlow sequenceFlow : sequenceFlowList){
            String conditionExpression = sequenceFlow.getConditionExpression();
            boolean flag = this.customELParser(conditionExpression,executeId);
            //如果flag的值为true则说明当前输出迁移线为排他网关后的下一步处理人
            if(flag){
                FlowElement nextNode = sequenceFlow.getTargetFlowElement();
                if(nextNode instanceof UserTask){
                    UserTask userTask = (UserTask) nextNode;
                    //找到用户任务节点，跳出方法并返回该任务节点的办理人
                    return userTask.getCategory();
                }
                //如果排他网关的下一个任务节点仍然是排他网关 执行递归操作 直至找到用户任务节点为止
                else if(nextNode instanceof ExclusiveGateway){
                    findGateWayTargetNode(flowElement,executeId);
                }
            }
        }
        return null;
    }

    /**
    *@description 分析迁移线上的EL表达式，为得到迁移线流向作为基础（用于排他网关）
    *@author  fangjiang
    *@date 2018/4/23 16:18
    */

    public boolean customELParser(String conditionExpression, String executeId) throws SnailException {
        Integer arg0 = 0;
        String[] tempArr={};
        if(conditionExpression.startsWith(ELConst.START_EXPRESSION)&& conditionExpression.endsWith(ELConst.END_EXPRESSION)){
            conditionExpression = conditionExpression.replace(ELConst.START_EXPRESSION,"").replace(ELConst.END_EXPRESSION,"");
            if(conditionExpression.contains(ELConst.GREATER_EQUAL)){
                tempArr = conditionExpression.split(ELConst.GREATER_EQUAL);
                try{
                    arg0 = Integer.parseInt(runtimeService.getVariable(executeId,tempArr[0]).toString());
                }catch (RuntimeException e){
                    throw new SnailException("超过预期的EL表达式解析范围,故此造成数组越界异常",e);
                }
                return arg0 >= Integer.parseInt(tempArr[1]);
            }
            else if(conditionExpression.contains(ELConst.GREATER_THAN)){
                tempArr = conditionExpression.split(ELConst.GREATER_THAN);
                try{
                    arg0 = Integer.parseInt(runtimeService.getVariable(executeId,tempArr[0]).toString());
                }catch (RuntimeException e){
                    throw new SnailException("超过预期的EL表达式解析范围,故此造成数组越界异常",e);
                }
                return arg0 > Integer.parseInt(tempArr[1]);
            }
            else if(conditionExpression.contains(ELConst.LESS_EQUAL)){
                tempArr = conditionExpression.split(ELConst.LESS_EQUAL);
                try{
                    arg0 = Integer.parseInt(runtimeService.getVariable(executeId,tempArr[0]).toString());
                }catch (RuntimeException e){
                    throw new SnailException("超过预期的EL表达式解析范围,故此造成数组越界异常",e);
                }
                return arg0 <= Integer.parseInt(tempArr[1]);
            }
            else if(conditionExpression.contains(ELConst.LESS_THAN)){
                tempArr = conditionExpression.split(ELConst.LESS_THAN);
                try{
                    arg0 = Integer.parseInt(runtimeService.getVariable(executeId,tempArr[0]).toString());
                }catch (RuntimeException e){
                    throw new SnailException("超过预期的EL表达式解析范围,故此造成数组越界异常",e);
                }
                return arg0 < Integer.parseInt(tempArr[1]);
            }

            else if(conditionExpression.contains(ELConst.EQUAL)){
                tempArr = conditionExpression.split(ELConst.EQUAL);
                try{
                    arg0 = Integer.parseInt(runtimeService.getVariable(executeId,tempArr[0]).toString());
                }catch (RuntimeException e){
                    throw new SnailException("超过预期的EL表达式解析范围,故此造成数组越界异常",e);
                }
                return arg0 == Integer.parseInt(tempArr[1]);
            }
            else if(conditionExpression.contains(ELConst.NOT_EQUAL)){
                tempArr = conditionExpression.split(ELConst.NOT_EQUAL);
                try{
                    arg0 = Integer.parseInt(runtimeService.getVariable(executeId,tempArr[0]).toString());
                }catch (RuntimeException e){
                    throw new SnailException("超过预期的EL表达式解析范围,故此造成数组越界异常",e);
                }
                return arg0 != Integer.parseInt(tempArr[1]);
            }
        }else {
           logger.error("EL表达式格式有问题，请检查流程定义文件中表达式");
        }
        return false;
    }

    /**
    *@description 判断当前节点是否是第一个用户任务节点
    *@author  fangjiang
    *@date 2018/4/24 11:31
    */

    public boolean isFirstUserTask(String processInstanceId){
        //得到所有输入迁移线信息 只要有一条输入的迁移线的名称为开始 则认为是第一个任务节点
        List<SequenceFlow> incomingFlows = this.getInTransition(processInstanceId);
        for(SequenceFlow flow : incomingFlows){
            if("开始".equals(flow.getName())){
                return true;
            }
        }
        return false;
    }

    /**
    *@description 判断当前节点是否是最后一个节点
    *@author  fangjiang
    *@date 2018/4/24 15:05
    */
    public boolean isLastUserTask(String processInstanceId){
        //得到所有输出迁移线信息 只要有一条输出的迁移线的名称为结束
        List<SequenceFlow> outFlows = this.getOutTransition(processInstanceId);
        for(SequenceFlow flow : outFlows){
            if("结束".equals(flow.getName())){
                return true;
            }
        }
        return false;
    }

    /**
    *@description 生成高亮节点和高亮迁移线的流程跟踪图
    *@author  fangjiang
    *@date 2018/4/25 9:45
    */

    public InputStream processMonitorImage(String processInstanceId){
        //获取历史流程实例
        HistoricProcessInstance historicProcessInstance =
                historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //获取所有已经执行过的活动节点 按照先后顺序排序
        List<HistoricActivityInstance> activityInstanceList =
                historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByActivityId().asc().list();
        //构造所有已经执行过得活动节点ID集合
        List<String> executedActivityIdList = new ArrayList<String>();
        for(HistoricActivityInstance activityInstance : activityInstanceList){
            executedActivityIdList.add(activityInstance.getActivityId());
        }
        //获取bpmn
        BpmnModel bpmnModel =
                repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        //已经流转的迁移线集合
        List<String> flowIds = this.getExecutedFlows(bpmnModel,activityInstanceList);
//        ProcessDiagramGenerator processDiagramGenerator =
//                processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
        ProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        InputStream inputStream = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel,diagramGenerator.getDefaultActivityFontName(),diagramGenerator.getDefaultLabelFontName(),diagramGenerator.getDefaultAnnotationFontName());
        return inputStream;
    }

    /**
    *@description 获取流转过得迁移线ID集合
    *@author  fangjiang
    *@date 2018/4/25 10:17
    */

    private List<String> getExecutedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances) {
        // 流转线ID集合
        List<String> flowIdList = new ArrayList<String>();
        // 全部活动实例
        List<FlowNode> historicFlowNodeList = new LinkedList<FlowNode>();
        // 已完成的历史活动节点
        List<HistoricActivityInstance> finishedActivityInstanceList = new LinkedList<HistoricActivityInstance>();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            historicFlowNodeList.add((FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true));
            if (historicActivityInstance.getEndTime() != null) {
                finishedActivityInstanceList.add(historicActivityInstance);
            }
        }

        // 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
        FlowNode currentFlowNode = null;
        for (HistoricActivityInstance currentActivityInstance : finishedActivityInstanceList) {
            // 获得当前活动对应的节点信息及outgoingFlows信息
            currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentActivityInstance.getActivityId(), true);
            List<SequenceFlow> sequenceFlowList = currentFlowNode.getOutgoingFlows();

            /**
             * 遍历outgoingFlows并找到已已流转的
             * 满足如下条件认为已已流转：
             * 1.当前节点是并行网关或包含网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已流转
             * 2.当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最近的流转节点视为有效流转
             */
            FlowNode targetFlowNode = null;
            if (BpmsActivityTypeEnum.PARALLEL_GATEWAY.getType().equals(currentActivityInstance.getActivityType())
                    || BpmsActivityTypeEnum.INCLUSIVE_GATEWAY.getType().equals(currentActivityInstance.getActivityType())) {
                // 遍历历史活动节点，找到匹配Flow目标节点的
                for (SequenceFlow sequenceFlow : sequenceFlowList) {
                    targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef(), true);
                    if (historicFlowNodeList.contains(targetFlowNode)) {
                        flowIdList.add(sequenceFlow.getId());
                    }
                }
            } else {
                List<Map<String, Object>> tempMapList = new LinkedList<Map<String,Object>>();
                // 遍历历史活动节点，找到匹配Flow目标节点的
                for (SequenceFlow sequenceFlow : sequenceFlowList) {
                    for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
                        if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
                            tempMapList.add(UtilMisc.toMap("flowId", sequenceFlow.getId(), "activityStartTime", String.valueOf(historicActivityInstance.getStartTime().getTime())));
                        }
                    }
                }

                // 遍历匹配的集合，取得开始时间最早的一个
                long earliestStamp = 0L;
                String flowId = null;
                for (Map<String, Object> map : tempMapList) {
                    long activityStartTime = Long.valueOf(map.get("activityStartTime").toString());
                    if (earliestStamp == 0 || earliestStamp >= activityStartTime) {
                        earliestStamp = activityStartTime;
                        flowId = map.get("flowId").toString();
                    }
                }
                flowIdList.add(flowId);
            }
        }
        return flowIdList;
    }





}
