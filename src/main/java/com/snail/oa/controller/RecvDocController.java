package com.snail.oa.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.snail.oa.activiti.ActivitiUtil;
import com.snail.oa.entity.*;
import com.snail.oa.exception.SnailException;
import com.snail.oa.service.*;
import com.snail.oa.util.ControlTool;
import com.snail.oa.util.LoggerUtil;
import com.snail.oa.util.PageUtil;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/1.
 */
@RequestMapping("recvdoc")
@Controller
public class RecvDocController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    //业务服务接口
    @Autowired
    private IRecvDocService recvDocService;
    //表单与流程设置服务接口
    @Autowired
    private IFormProcessService formProcessService;
    //表单服务接口
    @Autowired
    private IFormService formService;
    //流程运行服务
    @Autowired
    private RuntimeService runtimeService;
    //流程任务服务
    @Autowired
    private TaskService taskService;
    //用户服务
    @Autowired
    private IUserService userService;
    //activiti工具类
    @Autowired
    private ActivitiUtil activitiUtil;
    //结构服务
    @Autowired
    private IOrganizationService organizationService;
    //流程服务
    @Autowired
    private IWorkflowService workflowService;
    @Autowired
    private HistoryService historyService;
    /**
    *@description 保存业务数据
    *@author  fangjiang
    *@date 2018/4/1 14:27
    */

    @ResponseBody
    @RequestMapping("/saveRecvDoc")
    public String saveRecvDoc(HttpServletRequest request,HttpServletResponse response,
                              HttpSession session){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        RecvDoc recvDoc = jsonObject.toJavaObject(RecvDoc.class);
        Object object = session.getAttribute("user");
        User user = (User) session.getAttribute("user");
        recvDoc.setUserId(user.getId());
        recvDoc.setDraft("1");
        int result = recvDocService.saveOrUpdate(recvDoc);
        if(result>0){
            return "success";
        }
        return "fail";
    }

    /**
    *@description 启动流程
    *@author  fangjiang
    *@date 2018/4/5 17:27
    */
    @ResponseBody
    @RequestMapping("/startProcess")
    public String startProcess(HttpServletRequest request,HttpServletResponse response,
                             HttpSession session){
         JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
         RecvDoc recvDoc = jsonObject.toJavaObject(RecvDoc.class);
         Object object = session.getAttribute("user");
         User user = (User) object;
         recvDoc.setUserId(user.getId());
        //启动前先保存流程表单数据
        Map<String,Object> variables = new HashMap<String, Object>();
        variables.put("applyUser",user.getId());
        if(recvDoc.getMoney()!=null){
            variables.put("money",recvDoc.getMoney());
        }
        if(recvDoc.getSalary()!=null){
            variables.put("salary",recvDoc.getSalary());
        }
        ProcessInstance processInstance =
        runtimeService.startProcessInstanceById(recvDoc.getPdi(),variables);
        recvDoc.setPid(processInstance.getId());
        recvDoc.setDraft("0");
        recvDoc.setTakeActor(user.getId());
        recvDocService.saveOrUpdate(recvDoc);
        if(processInstance!=null){
            return "success";
        }
        return "fail";
    }

    /**
    *@description 获取签收列表
    *@author  fangjiang
    *@date 2018/4/9 11:23
    */

    @ResponseBody
    @RequestMapping("/signDoc")
    public PageUtil<RecvDoc> signDoc(HttpServletRequest request,HttpServletResponse response,
                                 HttpSession session){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam searchViewParam = jsonObject.toJavaObject(SearchViewParam.class);
        User user = userService.getCurrentUser(session);
        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateUser(user.getId());
        List<Task> taskList = taskQuery.list();
        StringBuilder ids = new StringBuilder("");
        for (Task task : taskList){
            ids.append("'").append(task.getProcessInstanceId()).append("',");
        }
        if(ids.length()>0){
         ids.deleteCharAt(ids.length()-1);
        }else {
            return new PageUtil<RecvDoc>(0,new ArrayList<RecvDoc>());
        }
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("ids",ids.toString());
        paraMap.put("userId",user.getId());
        PageInfo<RecvDoc> pageInfo =
        recvDocService.findSignTask(searchViewParam.pageNumber,searchViewParam.rowsCount,paraMap);
        return new PageUtil<RecvDoc>(pageInfo);

    }


    /**
    *@description  获取待办任务
    *@author  fangjiang
    *@date 2018/4/1 14:33
    */
    @ResponseBody
    @RequestMapping("/todoDoc")
    public PageUtil<RecvDoc> todoDoc(HttpServletRequest request,HttpServletResponse response,
                                 HttpSession session){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam searchViewParam = jsonObject.toJavaObject(SearchViewParam.class);
        User user = userService.getCurrentUser(session);
        Map<String,String> paraMap = new HashMap<String,String>();
        paraMap.put("userId",user.getId());
        String processName = searchViewParam.condition;
        if(processName==null||"".equals(processName)){
            processName = null;
        }
        paraMap.put("processName",processName);
        PageInfo<RecvDoc> pageInfo =
        recvDocService.findTodoTask(searchViewParam.pageNumber,searchViewParam.rowsCount,paraMap);
        return  new PageUtil<RecvDoc>(pageInfo);
    }

    /**
    *@description 获取在办任务
    *@author  fangjiang
    *@date 2018/4/1 14:34
    */

    @ResponseBody
    @RequestMapping("/processDoc")
    public PageUtil<RecvDoc> processDoc(HttpServletRequest request,HttpServletResponse response,
                                    HttpSession session){
        User user = userService.getCurrentUser(session);
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam searchViewParam= jsonObject.toJavaObject(SearchViewParam.class);
        Map<String,String> paraMap = new HashMap<String, String>();
        String processName = searchViewParam.condition;
        if(processName==null||"".equals(processName.trim())){
            processName = null;
        }
        paraMap.put("processName",processName);
        paraMap.put("userId",user.getId());
        PageInfo<RecvDoc> pageInfo =
          recvDocService.findProcessTask(searchViewParam.pageNumber,searchViewParam.rowsCount,paraMap);
        return new PageUtil<RecvDoc>(pageInfo);
    }

    /**
    *@description 获取办结任务
    *@author  fangjiang
    *@date 2018/4/1 14:35
    */
    @ResponseBody
    @RequestMapping("/processedDoc")
    public PageUtil<RecvDoc> processedDoc(HttpServletRequest request,HttpServletResponse response,
                                      HttpSession session){
        User user = userService.getCurrentUser(session);
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam searchViewParam= jsonObject.toJavaObject(SearchViewParam.class);
        Map<String,String> paraMap = new HashMap<String, String>();
        String processName = searchViewParam.condition;
        if(processName==null||"".equals(processName.trim())){
            processName = null;
        }
        paraMap.put("processName",processName);
        paraMap.put("userId",user.getId());
        PageInfo<RecvDoc> pageInfo =
                recvDocService.findProcessedTask(searchViewParam.pageNumber,searchViewParam.rowsCount,paraMap);
        return new PageUtil<RecvDoc>(pageInfo);
    }

    /**
    *@description 获取草稿列表
    *@author  fangjiang
    *@date 2018/4/1 14:36
    */
    @ResponseBody
    @RequestMapping("/getDraftDoc")
    public PageUtil<RecvDoc> getDraftDoc(HttpServletRequest request, HttpServletResponse response,
                                HttpSession session){
        User user = userService.getCurrentUser(session);
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam searchViewParam = jsonObject.toJavaObject(SearchViewParam.class);
        PageInfo<RecvDoc> pageInfo =
        recvDocService.getDraftDoc(user.getId(),searchViewParam.pageNumber,searchViewParam.rowsCount);
        return new PageUtil<RecvDoc>(pageInfo);
    }

    /**
    *@description 获取回收站数据
    *@author  fangjiang
    *@date 2018/4/1 14:40
    */

    @ResponseBody
    @RequestMapping("/getAbandonDoc")
    public PageUtil<RecvDoc> getAbandonDoc(HttpServletRequest request,HttpServletResponse response,
                                       HttpSession session){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam searchViewParam = jsonObject!=null?jsonObject.toJavaObject(SearchViewParam.class):null;
        String processName = searchViewParam.condition;
        if(processName==null||"".equals(processName.trim())){
            processName = null;
        }
        User user = userService.getCurrentUser(session);
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("userId",user.getId());
        paraMap.put("processName",processName);
        PageUtil<RecvDoc> pageUtil =
           recvDocService.findAbandonDoc(searchViewParam.pageNumber,searchViewParam.rowsCount,paraMap);
        return pageUtil;
    }

    /**
    *@description 流程办理视图
    *@author  fangjiang
    *@date 2018/4/22 14:15
    */

    @RequestMapping("/input")
    public ModelAndView input(HttpServletRequest request,HttpServletResponse response){
        String type = request.getParameter("type");
        String taskId = request.getParameter("taskId");
        ModelAndView modelAndView = new ModelAndView();
        RecvDoc recvDoc = new RecvDoc();
        if(type!=null&&"edit".equals(type.trim())){
            String id = request.getParameter("id");
            recvDoc = recvDocService.getRecvDocById(id);
            //判断当前节点是否是第一个用户任务节点
            boolean isFirstUserTask = activitiUtil.isFirstUserTask(recvDoc.getPid());
            if(isFirstUserTask){
                modelAndView.addObject("isFirstUserTask","true");
            }else {
                modelAndView.addObject("isFirstUserTask","false");
            }
            boolean isLastUserTask = activitiUtil.isLastUserTask(recvDoc.getPid());
            if(isLastUserTask){
               modelAndView.addObject("isLastUserTask","true");
            }else {
               modelAndView.addObject("isLastUserTask","false");
            }
            recvDoc.setTaskId(taskId);
            if(recvDoc.getPid()!=null&&!"".equals(recvDoc.getPid())){
              modelAndView.addObject("type","edit");
            }else {
                modelAndView.addObject("type","add");
            }
        }
        else if(type!=null&&"view".equals(type.trim())){
            String id = request.getParameter("id");
            recvDoc = recvDocService.getRecvDocById(id);
            recvDoc.setTaskId(taskId);
            modelAndView.addObject("type","view");
        }
        else {
            modelAndView.addObject("type","add");
        }
        //如果只是空的数据则显示流程列表 否则不显示
        modelAndView.addObject("recvDoc",recvDoc);
        modelAndView.addObject("recvDocJson", JSON.toJSON(recvDoc));
        modelAndView.setViewName("gwbl/recvDoc-input");
        return modelAndView;
    }

    /**
    *@description 给流程定义设置挂接表单(该功能需要修改) 2018-04-03注
    *@author  fangjiang
    *@date 2018/4/3 15:33
    */
    @ResponseBody
    @RequestMapping("/setFormForProcess")
    public String setFormForProcess(HttpServletRequest request,HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        FormProcess formProcess = jsonObject.toJavaObject(FormProcess.class);
        //检验之前的数据是否已经设置了表单
        FormProcess oldFormProcess = formProcessService.findByProcessId(formProcess.getId());
        if(oldFormProcess!=null){
            Integer result = formProcessService.updateFormId(formProcess);
            if(result>0){
                return "success";
            }
            return "fail";
        }else{
            int result = formProcessService.insertFormProcess(formProcess);
            if(result>0){
                return "success";
            }
            return "fail";
        }

    }

    /**
    *@description 根据流程定义ID获取表单信息
    *@author  fangjiang
    *@date 2018/4/3 15:32
    */

    @ResponseBody
    @RequestMapping("getFormInfoByPid")
    public Form getFormByProcessDefinitionId(HttpServletRequest request,HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        String processId = jsonObject.getString("processId");
        FormProcess formProcess =
        formProcessService.findByProcessId(processId);
        Form form = new Form();
        if(formProcess!=null){
            form = formService.findFormById(formProcess.getFormId());
        }
        return form;
    }

    /**
    *@description 得到下一步处理人信息(以树的形式展示 角色为父节点)
    *@author  fangjiang
    *@date 2018/4/6 14:39
    */
    @ResponseBody
    @RequestMapping("/getExecutionPerson")
    public List<TreeNode> getExecutionPerson(HttpServletRequest request,HttpServletResponse response) throws SnailException {
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        String pid = jsonObject.getString("pid");
        String taskId = jsonObject.getString("taskId");
        //首先获取下一个节点
        FlowNode flowNode = activitiUtil.getNextNode(pid);
        String assignee = "";
        if(flowNode instanceof UserTask){
            assignee  = ((UserTask)flowNode).getCategory();
        }
        else if(flowNode instanceof ExclusiveGateway){
            assignee = activitiUtil.findGateWayTargetNode(flowNode,pid);
        }
        if(assignee!=null&& assignee.contains("orgName")){
            String orgName = assignee.substring(assignee.indexOf("=")+1,assignee.length());
            List<User> userList = userService.findUserByOrgName(orgName);
            TreeNode parentNode = new TreeNode("","选择会签人员");
            List<TreeNode> childList = new ArrayList<TreeNode>();
            for(User user : userList){
                TreeNode child = new TreeNode(user.getId(),user.getName());
                child.setType("checkbox");
                childList.add(child);
            }
            parentNode.setChildren(childList);
            parentNode.setType("checkbox");
            List<TreeNode> treeNodes = new ArrayList<TreeNode>();
            treeNodes.add(parentNode);
            return treeNodes;
        }
        else if(assignee!=null&&assignee.contains("actorName")){
            String actorName = assignee.substring(assignee.indexOf("=")+1,assignee.length());
            List<Organization> organizationList = organizationService.findOrgByActorName(actorName);
            List<TreeNode> treeNodes = new ArrayList<TreeNode>();
            for (Organization organization : organizationList){
                TreeNode parentNode = new TreeNode(organization.getId(),organization.getName());
                List<TreeNode> childNodes = new ArrayList<TreeNode>();
                List<User> userList = organization.getUserList();
                for(User user : userList){
                    TreeNode child = new TreeNode(user.getId(),user.getName());
                    childNodes.add(child);
                }
                parentNode.setChildren(childNodes);
                treeNodes.add(parentNode);
            }
            return treeNodes;
        }else {
            logger.error("流程定义文件可能出错，未获取到组织机构名称或者处理人的角色名称");
            return new ArrayList<TreeNode>();
        }

    }

    /**
    *@description 流程处理
    *@author  fangjiang
    *@date 2018/4/6 14:48
    */
    @ResponseBody
    @RequestMapping("/gotoNext")
    public String gotoNext(HttpServletRequest request,HttpServletResponse response,
                           HttpSession session) throws SnailException {
        //任务办理之后 再设置下一步处理人
        //根据流程实例ID和办理人ID 将任务进行办理
        String result = "success";
        User user = userService.getCurrentUser(session);
        try {
            JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
            String type = jsonObject.getString("type");
            RecvDoc recvDoc = jsonObject.toJavaObject(RecvDoc.class);
            String taskId = recvDoc.getTaskId();
            String doType = jsonObject.getString("doType");
            if(doType!=null&&"sign".equals(doType.trim())){
                String[] ids = recvDoc.getTakeActor().split(",");
                for(String id : ids){
                   taskService.addCandidateUser(taskId,id);
                }
                taskService.setVariable(taskId,"gotoNext",recvDoc.getTakeActor());
            }else {
               taskService.setVariable(taskId,"gotoNext",recvDoc.getTakeActor());
            }
            //Map<String,Object> variables = new HashMap<String, Object>();
//            variables.put("gotoNext",recvDoc.getTakeActor());
//            taskService.addUserIdentityLink(taskId,recvDoc.getTakeActor(), IdentityLinkType.ASSIGNEE);
            //判断任务类型
            if("sign".equals(type)){
                taskService.claim(taskId,user.getId());
                logger.debug("流程名为: "+recvDoc.getProcessName()+" 已经被签收");
            }else {
//                String[] takeActors = recvDoc.getTakeActor().split(",");
//                if(takeActors.length>1){
//                    for(String takeActor:takeActors){
//                        taskService.addCandidateUser(taskId,takeActor);
//                    }
//                }else {
//                    //设置当前流程办理人
//                    taskService.setAssignee(taskId,recvDoc.getTakeActor());
//                }
                //办理流程
                taskService.complete(taskId);
                //保存业务数据
                recvDocService.saveOrUpdate(recvDoc);
                logger.debug("流程名为: "+recvDoc.getProcessName()+" 已经被办理");
            }

        }catch (Exception e){
            result = "fail";
            logger.error(LoggerUtil.error("流程处理遇到异常"));
            throw new SnailException("流程处理时遇到了异常    发生异常类:"+this.getClass().getName(),e);
        }finally {
            //无论是否发生异常 都给客户端返回信息
            return result;
        }
    }

    /**
    *@description 提供表单加载数据
    *@author  fangjiang
    *@date 2018/4/9 16:46
    */

    @ResponseBody
    @RequestMapping("/getRecvDocData")
    public RecvDoc getRecvDocData(HttpServletRequest request,HttpServletResponse response,
                                  HttpSession session){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        String ids = jsonObject.getString("pid");
        User user = userService.getCurrentUser(session);
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("ids",ids);
        paraMap.put("userId",user.getId());
        RecvDoc recvDoc =
        recvDocService.findTaskByProcessInstanceId(paraMap);
        return recvDoc;
    }

    /**
    *@description  文件回收
    *@author  fangjiang
    *@date 2018/4/13 16:26
    */

    @ResponseBody
    @RequestMapping("/abandonDoc")
    public String abandonDoc(HttpServletRequest request,HttpServletResponse response) throws SnailException {
        List<String> list = ControlTool.getJSONArray(request,response);
        Map<String,String> paraMap = new HashMap<String, String>();
        StringBuilder ids = new StringBuilder();
        for(String id : list){
            ids.append("'").append(id).append("',");
        }
       ids.deleteCharAt(ids.length()-1);
        paraMap.put("ids",ids.toString());
        String result = "fail";
        try {
            recvDocService.abandonDoc(paraMap);
            result = "success";
        }catch (Exception e){
           result = "fail";
            throw  new SnailException("文件删除错误",e);

        }
        return result;
    }

    /**
    *@description 还原文件
    *@author  fangjiang
    *@date 2018/4/13 16:52
    */

    @ResponseBody
    @RequestMapping("/recoverDoc")
    public String recoverDoc(HttpServletRequest request,HttpServletResponse response) throws SnailException {
        List<String> list = ControlTool.getJSONArray(request,response);
        Map<String,String> paraMap = new HashMap<String, String>();
        StringBuilder ids = new StringBuilder();
        for(String id : list){
            ids.append("'").append(id).append("',");
        }
        ids.deleteCharAt(ids.length()-1);
        paraMap.put("ids",ids.toString());
        String result = "fail";
        try {
             result = "success";
             recvDocService.recoverDoc(paraMap);
        }catch (Exception e){
            result = "fail";
            throw new SnailException("文件还原错误",e);
        }finally {
           return result;
        }

    }

    /**
    *@description 删除文件
    *@author  fangjiang
    *@date 2018/4/13 17:18
    */
    @ResponseBody
    @RequestMapping("/deleteRecvdoc")
    public String deleteRecvdoc(HttpServletRequest request,HttpServletResponse response){
        List<String> list = ControlTool.getJSONArray(request,response);
        int result = recvDocService.deleteRecvDoc(list);
        if(result>0){
            return "success";
        }
        return "fail";
    }

    /**
    *@description 获取流程跟踪图
    *@author  fangjiang
    *@date 2018/4/25 15:34
    */
    @RequestMapping("/processMonitor")
    public void processMonitor(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String processInstanceId = request.getParameter("pid");
        if(!"".equals(processInstanceId)){
          InputStream inputStream = activitiUtil.processMonitorImage(processInstanceId);
            File file = new File("C:\\Users\\Administrator\\Desktop\\test.png");
            FileUtils.copyInputStreamToFile(inputStream,file);
          ControlTool.responseImage(response,inputStream);

        }else {
            logger.error("获取流程跟踪图片失败");
        }

    }
}
