import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.snail.oa.entity.Line;
import com.snail.oa.entity.Node;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/3/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-*.xml"})
public class CustomTest {

    private static final String START = "start round mix";//开始节点
    private static final String END = "end round mix";//结束节点
    private static final String TASK = "task";//普通任务节点
    private static final String NODE = "node";//自动节点
    @Autowired
    RepositoryService repositoryService;
    @Test
    public void test(){
        List<String> list = new ArrayList<String>();
        list.add("aaaaaaa");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void demo() throws IOException {

        String filePath = this.getClass().getClassLoader().getResource("data2.js").getPath();
        File file = new File(filePath);
        String jsonString =  FileUtils.readFileToString(file,"utf-8");
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
       getBpmnModel(title,listNode,listLine);
    }

    public void getBpmnModel(String title,List<Node> listNode,List<Line> listLine) throws IOException {
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
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        byte[] bytes = bpmnXMLConverter.convertToXML(bpmnModel);
//        String xmlString = new String(bytes);
//        System.out.println(xmlString);
        ProcessValidatorFactory processValidatorFactory=new ProcessValidatorFactory();
        ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
        //验证失败信息的封装ValidationError
        List<ValidationError> validate = defaultProcessValidator.validate(bpmnModel);
        if(validate.size()==0){
            DeploymentBuilder builder = repositoryService.createDeployment();
            builder.addBytes(title,bytes);
            InputStream inputStream =
            this.getClass().getClassLoader().getResourceAsStream("newFlow_1.png");
            builder.addInputStream("newFlow_1",inputStream);
            Deployment deployment = builder.deploy();
//            builder.addBytes(bytes);
//            System.out.println(deployment.getId());
         //   Deployment deployment = builder.deploy();
            System.out.println(deployment.getId());
        }else {
            System.out.println("流程定义文件存在错误请修改之后再部署");
        }

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
    public void initFlowNode(FlowNode flowNode,List<Line> listLine,
                             List<SequenceFlow> inSequenceFlow,List<SequenceFlow> outSequenceFlow){
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

    @Test
    public void demo2(){
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource("qjlc.bpmn20.xml");
        builder.addClasspathResource("newFlow_1.png");
        Deployment deployment = builder.deploy();
        System.out.println(deployment.getId());

    }

}
