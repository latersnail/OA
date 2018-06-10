package com.snail.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.Form;
import com.snail.oa.entity.SearchViewParam;
import com.snail.oa.entity.TreeNode;
import com.snail.oa.service.IFormProcessService;
import com.snail.oa.service.IFormService;
import com.snail.oa.util.ControlTool;
import com.snail.oa.util.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by fangjiang on 2018/4/1.
 */
@RequestMapping("form")
@Controller
public class FormController {

    @Autowired
    private IFormProcessService formProcessService;
    @Autowired
    private IFormService formService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @ResponseBody
    @RequestMapping("/saveOrUpdateForm")
    public String saveOrUpdateForm(HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        Form form = jsonObject.toJavaObject(Form.class);
        int result = formService.saveOrUpdate(form);
        if(result>0){
            return "success";
        }
        return "fail";
    }

    @ResponseBody
    @RequestMapping("/findFormByPage")
    public PageUtil<Form> findFormByPage(HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam searchViewParam = jsonObject.toJavaObject(SearchViewParam.class);
        String formName = searchViewParam.condition;
        if(formName==null||"".equals(formName.trim())){
            formName = null;
        }
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("formName",formName);
        PageInfo<Form> pageInfo =
        formService.findFormByPage(searchViewParam.pageNumber,searchViewParam.rowsCount,paraMap);
        return new PageUtil<Form>(pageInfo);
    }

    @ResponseBody
    @RequestMapping("/deleteForm")
    public String deleteForm(HttpServletRequest request, HttpServletResponse response){
        List<String> list = ControlTool.getJSONArray(request,response);
        int result = formService.deleteForm(list);
        if(result>0){
            //级联删除 该表单被所有挂接的数据
            int cascadeResult = formProcessService.deleteByFormId(list);
            logger.info("删除表单的同时,已经自动级联删除了所有流程挂接该表单的数据,一共"+cascadeResult+"条");
            return "success";
        }
        return "fail";
    }

    /**
    *@description 检验资源地址的合法性
    *@author  fangjiang
    *@date 2018/4/2 9:45
    */
    @ResponseBody
    @RequestMapping("/checkSourcePath")
    public String checkSourcePath(HttpServletRequest request,HttpServletResponse response) throws MalformedURLException {
       JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
       String path = jsonObject.getString("path");
        if(path!=null){
            URL url= request.getServletContext().getResource(path);
            if(url!=null){
                return "表单地址合法";
            }
            else {
                return "表单地址不合法";
            }
        }else {
            return "表单地址错误";
        }

    }

    /**
    *@description 提供服务器的时间
    *@author  fangjiang
    *@date 2018/4/2 22:18
    */
    @ResponseBody
    @RequestMapping("/getServerTime")
    public String getServerTime(){
       return ControlTool.formatData(new Date(),"yyyy-MM-dd HH:mm:ss");
    }

    /**
    *@description 提供表单树 供流程设置表单使用
    *@author  fangjiang
    *@date 2018/4/3 20:16
    */
    @ResponseBody
    @RequestMapping("/getFormTree")
    public List<TreeNode> getFormTree(){
       TreeNode parentNode = new TreeNode("","流程表单");
       List<TreeNode> childs = new ArrayList<TreeNode>();
       List<Form> forms = formService.findFormList(null);
        for (Form form:forms) {
            TreeNode treeNode = new TreeNode(form.getId(),form.getFormName());
            childs.add(treeNode);
        }
        parentNode.setChildren(childs);
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        nodes.add(parentNode);
        return  nodes;
    }

    /**
    *@description 根据表单ID获取表单信息
    *@author  fangjiang
    *@date 2018/4/9 19:16
    */
    @ResponseBody
    @RequestMapping("/getFormById")
    public Form getFormById(HttpServletRequest request,HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        String formId = jsonObject.getString("formId");
        Form form = formService.findFormById(formId);
        return  form;
    }



}
