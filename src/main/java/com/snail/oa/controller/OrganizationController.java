package com.snail.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.Organization;
import com.snail.oa.entity.SearchViewParam;
import com.snail.oa.entity.TreeNode;
import com.snail.oa.service.IOrganizationService;
import com.snail.oa.util.ControlTool;
import com.snail.oa.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/3/27.
 */
@Controller
@RequestMapping("organization")
public class OrganizationController implements BaseController{

    @Autowired
    private IOrganizationService organizationService;

    @ResponseBody
    @RequestMapping("/findOrganizationByPage")
    public PageUtil<Organization> findOrganizationByPage(HttpServletRequest request,
                                                     HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam searchViewParam = jsonObject.toJavaObject(SearchViewParam.class);
        String name = searchViewParam.condition;
        if(name==null||"".equals(name.trim())){
           name = null;
        }
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("name",name);
        PageInfo<Organization> pageInfo =
        organizationService.findOrganizationByPage(searchViewParam.pageNumber,searchViewParam.rowsCount,paraMap);
      return new PageUtil<Organization>(pageInfo);
    }

    @RequestMapping("/input")
    public ModelAndView input(HttpServletRequest request){
        String type = request.getParameter("type");
        String id = request.getParameter("id");
        ModelAndView modelAndView = new ModelAndView();
        if(type!=null&&"edit".equals(type.trim())){
            Organization organization =
            organizationService.findOrganizationById(id);
            modelAndView.addObject("organization",organization);
            modelAndView.setViewName("/organization/organization-edit");
        }
        else if(type!=null&&"add".equals(type.trim())){
            modelAndView.setViewName("/organization/organization-add");
        }
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/insertOrganization")
    public String insertOrganization(HttpServletRequest request,HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        Organization organization = jsonObject.toJavaObject(Organization.class);
        int result = organizationService.insertOrganization(organization);
        if(result>0){
            return "success";
        }else{
            return "fail";
        }
    }

    @ResponseBody
    @RequestMapping("/updateOrganization")
    public String updateOrganization(HttpServletRequest request,HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        Organization organization = jsonObject.toJavaObject(Organization.class);
        int result = organizationService.updateOrganization(organization);
        if(result>0){
            return "success";
        }else{
            return "fail";
        }
    }

    @ResponseBody
    @RequestMapping("/deleteOrganization")
    public String deleteOrganization(HttpServletRequest request,HttpServletResponse response){
        List<String> list = ControlTool.getJSONArray(request,response);
        int result =
        organizationService.deleteOrganization(list);
        if(result>0){
            return "success";
        }else{
            return "fail";
        }
    }

    @ResponseBody
    @RequestMapping("/getOrgTree")
    public List<TreeNode> getOrgTree(){
        List<Organization> organizationList = organizationService.findOrganization(null);
        TreeNode parentNode = new TreeNode("","机构列表");
        List<TreeNode> childNodes = new ArrayList<TreeNode>();
        for(Organization org : organizationList){
            TreeNode childNode = new TreeNode(org.getId(),org.getName());
            childNodes.add(childNode);
        }
        parentNode.setChildren(childNodes);
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        treeNodes.add(parentNode);
        return treeNodes;
    }

    public String saveOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
         Organization organization = jsonObject.toJavaObject(Organization.class);
         int result = organizationService.saveOrUpdate(organization);
         if(result>0){
             return "success";
         }
        return "fail";
    }
}
