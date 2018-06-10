package com.snail.oa.util;

import com.alibaba.fastjson.JSONObject;
import com.snail.oa.entity.Line;
import com.snail.oa.entity.Node;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
*@description
* <p>基于BPMN2.0规范对前端传的数据进行解析</p>
 * <p>和对基于BPMN2.0规范的xml文档解析出前端所需的数据</p>
*@author  fangjiang
*@date 2018/3/28 17:12
*/

public class BPMNUtil {

    private static final String START = "start round mix";//开始节点
    private static final String END = "end round mix";//结束节点
    private static final String TASK = "task";//普通任务节点
    private static final String NODE = "node";//自动节点

    /**
    *@description 将json数据解析成BpmnModel
    *@author  fangjiang
    *@date 2018/3/30 14:13
    */

    public  BpmnModel parseJsonToBpmnModel(String jsonString){
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String title = jsonObject.getString("title");
        String nodesJson = jsonObject.getString("nodes");
        JSONObject nodesJsonObject = JSONObject.parseObject(nodesJson);
        Map<String, Object> nodesMapJson = nodesJsonObject.getInnerMap();
        List<Node> listNode = new ArrayList<Node>();
        for (String key:nodesMapJson.keySet()){
            String nodeJson = nodesJsonObject.getString(key);
            Node node = JSONObject.parseObject(nodeJson, Node.class);
            node.setNodeId(key);
            listNode.add(node);
        }
        List<Line> listLine = new ArrayList<Line>();
        String linesJson = jsonObject.getString("lines");
        JSONObject linesJsonObject = JSONObject.parseObject(linesJson);
        Map<String,Object> linesMapJson = linesJsonObject.getInnerMap();
        for (String key : linesMapJson.keySet()) {
            String lineJson = linesJsonObject.getString(key);
            Line line = JSONObject.parseObject(lineJson, Line.class);
            line.setLineId(key);
            listLine.add(line);
        }
        return  initBpmnModel(title,listNode,listLine);
    }

    /**
    *@description 初始化bpmnModel数据
    *@author  fangjiang
    *@date 2018/3/30 14:13
    */

    public  BpmnModel initBpmnModel(String title,List<Node> listNode,List<Line> listLine){
        BpmnModel bpmnModel = new BpmnModel();
        StartEvent startEvent = null;
        EndEvent endEvent = null;
        Process process = new Process();
        //迁移线相向节点列表
        List<SequenceFlow> inSequenceFlow = new ArrayList<SequenceFlow>();
        //迁移线反向节点列表
        List<SequenceFlow> outSequenceFlow = new ArrayList<SequenceFlow>();
        //任务节点列表
        List<Task> taskList = new ArrayList<Task>();
        List<SequenceFlow> sequenceFlowList = new ArrayList<SequenceFlow>();
        for(Node node:listNode){
            if(TASK.equals(node.getType())){
                UserTask userTask = new UserTask();
                userTask.setName(node.getName());
                userTask.setId(node.getNodeId());
                initFlowNode(userTask,listLine,inSequenceFlow,outSequenceFlow);
                taskList.add(userTask);
                inSequenceFlow.clear();
                outSequenceFlow.clear();
            }
            else if(START.equals(node.getType())){
                startEvent = new StartEvent();
                startEvent.setId(node.getNodeId());
                startEvent.setName(node.getName());
                initFlowNode(startEvent,listLine,inSequenceFlow,outSequenceFlow);
                startEvent.setOutgoingFlows(outSequenceFlow);
                outSequenceFlow.clear();
            }
            else if(END.equals(node.getType().trim())){
                endEvent = new EndEvent();
                endEvent.setId(node.getNodeId());
                endEvent.setName(node.getName());
                initFlowNode(endEvent,listLine,inSequenceFlow,outSequenceFlow);
                inSequenceFlow.clear();
            }
            else if(NODE.equals(node.getType().trim())){
                ServiceTask serviceTask = new ServiceTask();
                serviceTask.setId(node.getNodeId());
                serviceTask.setName(node.getName());
                taskList.add(serviceTask);
                initFlowNode(serviceTask,listLine,inSequenceFlow,outSequenceFlow);
                inSequenceFlow.clear();
                outSequenceFlow.clear();
            }
        }
        process.setName(title);
        process.setExecutable(true);
        process.setId(title);
        process.addFlowElement(startEvent);
        for(Task task:taskList){
            process.addFlowElement(task);
        }
        process.addFlowElement(endEvent);
        for(Line line:listLine){
            SequenceFlow sequenceFlow = new SequenceFlow();
            sequenceFlow.setId(line.getLineId());
            sequenceFlow.setName(line.getName());
            sequenceFlow.setSourceRef(line.getFrom());
            sequenceFlow.setTargetRef(line.getTo());
            process.addFlowElement(sequenceFlow);
        }
        bpmnModel.addProcess(process);
       return bpmnModel;
    }

    /**
     *@description 初始化任务节点信息
     *@param flowNode 节点信息
     *@param listLine 迁移线信息
     *@param inSequenceFlow 节点迁移线流向集合
     *@param outSequenceFlow 节点迁移线流出集合
     *@author  fangjiang
     *@date 2018/3/29 22:20
     */
    public void initFlowNode(FlowNode flowNode, List<Line> listLine,
                             List<SequenceFlow> inSequenceFlow, List<SequenceFlow> outSequenceFlow){
        for (Line line:listLine) {
            if(flowNode.getId().equals(line.getTo())){
                SequenceFlow sequenceFlow = new SequenceFlow();
                sequenceFlow.setName(line.getName());
                sequenceFlow.setId(line.getLineId());
                sequenceFlow.setSourceRef(line.getFrom());
                sequenceFlow.setTargetRef(line.getTo());
                inSequenceFlow.add(sequenceFlow);
            }else if(flowNode.getId().equals(line.getFrom())){
                SequenceFlow sequenceFlow = new SequenceFlow();
                sequenceFlow.setName(line.getName());
                sequenceFlow.setId(line.getLineId());
                sequenceFlow.setSourceRef(line.getFrom());
                outSequenceFlow.add(sequenceFlow);
            }
        }
    }

    /**
    *@description 流程在部署前进行校验
    *@author  fangjiang
    *@date 2018/3/30 14:10
    */
    public static List<ValidationError> ValidatorBpmnModel(BpmnModel bpmnModel){
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        byte[] bytes = bpmnXMLConverter.convertToXML(bpmnModel);
        String xmlString = new String(bytes);
        ProcessValidatorFactory processValidatorFactory=new ProcessValidatorFactory();
        ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
        //验证失败信息的封装ValidationError
        List<ValidationError> validate = defaultProcessValidator.validate(bpmnModel);
        return validate;
    }

    /**
    *@description 生成BpmnXml
    *@author  fangjiang
    *@date 2018/3/30 17:04
    */
    public void geneteBpmnXml(String title,String jsonString) throws IOException {
        BpmnModel bpmnModel = parseJsonToBpmnModel(jsonString);
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        byte[] bytes = bpmnXMLConverter.convertToXML(bpmnModel);
        String bpmnXmlString = new String(bytes);
        String directoryName =
                this.getClass().getClassLoader().getResource("bpmnFile").getPath();
        File file = new File(directoryName+"/"+title+".bpm");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(bpmnXmlString);
        fileWriter.close();
    }

    /**
    *@description  部署流程定义文件
    *@author  fangjiang
    *@date 2018/3/30 17:42
    */
    public Deployment deploymentResource(String directory, DeploymentBuilder builder) throws IOException {
        //遍历bpmnFile目录下的所有文件
        String directoryName =
                this.getClass().getClassLoader().getResource(directory).getPath();
        File file = new File(directoryName);
        //判断是否是文件夹
        if(file.isDirectory()){
           File[] fileList = file.listFiles();
           for (File fileItem : fileList){
              builder.addClasspathResource(fileItem.getPath());
           }
        }
       return builder.deploy();
    }

    public BpmnModel  newInstace(){
        return new BpmnModel();
    }
}
