package com.snail.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.ProcessDefinitionVo;
import com.snail.oa.entity.ProcessInfo;
import com.snail.oa.entity.SearchViewParam;
import com.snail.oa.entity.TreeNode;
import com.snail.oa.exception.SnailException;
import com.snail.oa.service.IFormProcessService;
import com.snail.oa.service.IProcessInfoService;
import com.snail.oa.service.IWorkflowService;
import com.snail.oa.util.ControlTool;
import com.snail.oa.util.PageUtil;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * Created by fangjiang on 2018/3/28.
 */
@Controller
@RequestMapping(value = "processInfo")
public class ProcessInfoController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IFormProcessService formProcessService;
    @Autowired
    private IProcessInfoService processInfoService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IWorkflowService workflowService;
    /**
    *@description  保存和修改都执行该方法
    *@author  fangjiang
    *@date 2018/3/28 16:59
    */

    @ResponseBody
    @RequestMapping(value = "/saveOrUpdate")
    public String saveOrUpdateProcessInfo(HttpServletRequest request,
                                    HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        ProcessInfo processInfo = jsonObject.toJavaObject(ProcessInfo.class);
        int result = processInfoService.saveOrUpdate(processInfo);
        if(result>0){
            return "success";
        }
        return "fail";
    }

    @ResponseBody
    @RequestMapping(value = "/findProcessInfoList")
    public PageUtil<ProcessInfo> findProcessInfoList (HttpServletRequest request,
                                                      HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam searchViewParam = jsonObject.toJavaObject(SearchViewParam.class);
        PageInfo<ProcessInfo> pageInfo =
                        processInfoService.findProcessInfoList(searchViewParam.pageNumber,
                        searchViewParam.rowsCount,searchViewParam.condition);
        return new PageUtil<ProcessInfo>(pageInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteProcessInfo")
    public String deleteProcessInfo(HttpServletRequest request,
                                    HttpServletResponse response){
       List<String> list = ControlTool.getJSONArray(request,response);
       int result = processInfoService.deleteProcessInfo(list);
        if(result>0){
            return "success";
        }
        return "fail";
    }

    /**
    *@description 流程设计与重设计响应模块
    *@author  fangjiang
    *@date 2018/3/28 17:03
    */

    @RequestMapping(value = "/processDesignView")
    public ModelAndView processDesignView(HttpServletRequest request,
                                          HttpServletResponse response){
        String procdefId = request.getParameter("procdefId");
        ModelAndView modelAndView = new ModelAndView();
        //
        if(procdefId==null||"".equals(procdefId.trim())){

        }else {

        }
        return modelAndView;
    }

    /**
    *@description 流程定义文件上传
    *@author  fangjiang
    *@date 2018/4/22 20:49
    */

    @RequestMapping("/uploadProcess")
    public void uploadProcess(@RequestParam("file")CommonsMultipartFile file,HttpServletResponse response,
        HttpServletRequest request){
        DeploymentBuilder builder = repositoryService.createDeployment();
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = ControlTool.getResponseOut(response);
        try {
            builder.addZipInputStream(new ZipInputStream(file.getInputStream()));
            Deployment deployment = builder.deploy();
            if(deployment==null||deployment.getId()==null||"".equals(deployment.getId().trim())){
              out.write("<script>window.parent.callback('fail');</script>");
            }
        } catch (IOException e) {
            e.printStackTrace();
            out.write("<script>window.parent.callback('fail');</script>");
        }
        out.write("<script>window.parent.callback('success');</script>");
    }

    @ResponseBody
    @RequestMapping("/getProcessDefinitionList")
    public PageUtil<ProcessDefinitionVo> getProcessDefinitionList(HttpServletRequest request,
                                                             HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam searchViewParam = jsonObject.toJavaObject(SearchViewParam.class);
        ProcessDefinitionQuery query =
        repositoryService.createProcessDefinitionQuery();
        if(searchViewParam.condition!=null&&!"".equals(searchViewParam.condition.trim())){
            query.processDefinitionNameLike(searchViewParam.condition);
        }
        int pageNumber = searchViewParam.pageNumber;
        int pageRows = searchViewParam.rowsCount;
        int firstResult = (pageNumber-1)*pageRows;
        int maxResult = (pageNumber+1)*pageRows;
        List<ProcessDefinition> processDefinitions = query.listPage(firstResult,maxResult);
        List<ProcessDefinitionVo> processDefinitionVos = new ArrayList<ProcessDefinitionVo>();
        for (ProcessDefinition p:processDefinitions) {
           ProcessDefinitionVo processDefinitionVo = new ProcessDefinitionVo(p.getId(),p.getName(),
           p.getDescription(),p.getKey(),p.getVersion(),p.getCategory(),p.getDeploymentId(),p.getResourceName(),
           p.getTenantId(),p.getDiagramResourceName());
           processDefinitionVos.add(processDefinitionVo);
        }
        long total = query.list().size();
        return  new PageUtil<ProcessDefinitionVo>(total,processDefinitionVos);
    }

    @ResponseBody
    @RequestMapping("/deleteDefinition")
    public String deleteDefinition(HttpServletRequest request,HttpServletResponse response) throws SnailException {
        List<String> list = ControlTool.getJSONArray(request,response);
        if(list==null||list.size()==0)return "fail";
        try{
            ProcessDefinition processDefinition =
                repositoryService.createProcessDefinitionQuery().processDefinitionId(list.get(0)).singleResult();
            //级联删除流程部署和流程定义文件。
            repositoryService.deleteDeployment(processDefinition.getDeploymentId(),true);
            //级联删除该该流程与表单挂接的数据
            formProcessService.deleteByProcessId(list);
            logger.info("在删除流程定义数据的同时也删除了流程与表关联的数据");
            return "success";
        }catch (Exception e){
            throw new SnailException("流程定义文件删除失败",e);
        }
    }

    /**
    *@description 获取流程列表树
    *@author  fangjiang
    *@date 2018/4/1 10:37
    */

    @ResponseBody
    @RequestMapping(value = "getProcessTree")
    public List<TreeNode> getProcessTree(){
        TreeNode parentNode = new TreeNode("","流程列表");
        List<TreeNode> childNode = new ArrayList<TreeNode>();
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> processDefinitions = query.list();
        for (ProcessDefinition processDefinition:processDefinitions){
            TreeNode treeNode = new TreeNode(processDefinition.getId(),processDefinition.getName());
            childNode.add(treeNode);
        }
        parentNode.setChildren(childNode);
        List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
        treeNodeList.add(parentNode);
     return treeNodeList;
    }

    /**
     *@description 获取流程部署资源 图片和定义文件信息
     *@author  fangjiang
     *@date 2018/4/21 22:22
     */

    @RequestMapping(value = "/getDeploymentSource",method = {RequestMethod.GET,RequestMethod.POST})
    public void getDeploymentSource(HttpServletRequest request,HttpServletResponse response){
        String type = request.getParameter("type");
        String processDefinitionId = request.getParameter("processDefinitionId");
        if(type!=null&&"image".equals(type)){
            InputStream in = workflowService.getProcessImage(processDefinitionId);
            try {
                ControlTool.responseImage(response,in);
            } catch (IOException e) {
                logger.error("IO异常",e);
            }
        }
        else if(type!=null&&"xml".equals(type)){
            InputStream in = workflowService.getProcessXml(processDefinitionId);
            try {
                response.setContentType("text/html");
                response.setCharacterEncoding("utf-8");
                String xmlStr = IOUtils.toString(in,"utf-8");
                response.getWriter().write(xmlStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }






}
