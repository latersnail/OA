package com.snail.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.SearchViewParam;
import com.snail.oa.entity.Source;
import com.snail.oa.entity.SourceGroup;
import com.snail.oa.entity.TreeNode;
import com.snail.oa.service.ISourceGroupService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangjiang on 2018/4/11.
 */
    @Controller
    @RequestMapping("sourceGroup")
    public class SourceGroupController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ISourceGroupService sourceGroupService;

    @ResponseBody
    @RequestMapping("/saveOrUpdate")
    public String saveOrUpdate(HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SourceGroup sourceGroup = jsonObject.toJavaObject(SourceGroup.class);
        Integer result = sourceGroupService.saveOrUpdate(sourceGroup);
        if(result>0){
            return "success";
        }
        return "fail";
    }

    @ResponseBody
    @RequestMapping("/deleteSourceGroup")
    public String deleteSourceGroup(HttpServletRequest request,HttpServletResponse response){
        List<String> list = ControlTool.getJSONArray(request,response);
        Integer result = sourceGroupService.deleteSourceGroup(list);
        if(result>0){
          return "success";
        }
        return "fail";
    }

    @ResponseBody
    @RequestMapping("/findSourceGroupByPage")
    public PageUtil<SourceGroup> findSourceGroupByPage(HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam searchViewParam = jsonObject.toJavaObject(SearchViewParam.class);
       PageInfo<SourceGroup> pageInfo = sourceGroupService.findSourceGroupByPage(searchViewParam.pageNumber,searchViewParam.rowsCount,searchViewParam.condition);
        PageUtil<SourceGroup> pageUtil = new PageUtil<SourceGroup>(pageInfo.getTotal(),pageInfo.getList());
       return pageUtil;
    }

    @ResponseBody
    @RequestMapping("/findSourceGroupById")
    public SourceGroup findSourceGroupById(HttpServletRequest request,HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        String id = jsonObject.getString("id");
        return sourceGroupService.findSourceGroupById(id);
    }

    /**
    *@description 得到资源组树
    *@author  fangjiang
    *@date 2018/4/14 22:52
    */

    @ResponseBody
    @RequestMapping("/getGroupTree")
    public List<TreeNode> getGroupTree(){
       List<SourceGroup> sourceGroupList = sourceGroupService.findSourceGroupByList();
       TreeNode parentNode = new TreeNode("","资源组");
       List<TreeNode> childNodes = new ArrayList<TreeNode>();
       for(SourceGroup sourceGroup:sourceGroupList){
           TreeNode childNode = new TreeNode(sourceGroup.getId(),sourceGroup.getName());
           childNodes.add(childNode);
       }
        parentNode.setChildren(childNodes);
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        nodes.add(parentNode);
        return nodes;
    }

    /**
    *@description 检测资源
    *@author  fangjiang
    *@date 2018/4/14 22:52
    */

    @ResponseBody
    @RequestMapping("/checkSource")
    public String checkSource(HttpServletRequest request,HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        String sourcePath = jsonObject != null ? jsonObject.getString("sourcePath") : null;
        String result = "fail";
        try {
            URL url= request.getServletContext().getResource(sourcePath);
            if(url!=null){
                result = "success";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
    *@description 得到资源树
    *@author  fangjiang
    *@date 2018/4/15 10:04
    */

    @ResponseBody
    @RequestMapping("getSourceByGroup")
    public List<TreeNode> getSourceByGroup(){
       List<SourceGroup> sourceGroupList = sourceGroupService.findSourceByGroup();
       List<TreeNode> treeNodes = new ArrayList<TreeNode>();
       for(SourceGroup sourceGroup:sourceGroupList){
           TreeNode parent = new TreeNode(sourceGroup.getId(),sourceGroup.getName());
           List<Source> sourceList = sourceGroup.getSourceList();
           List<TreeNode> childList = new ArrayList<TreeNode>();
           for(Source source:sourceList){
               TreeNode child = new TreeNode(source.getId(),source.getName());
               childList.add(child);
           }
           parent.setChildren(childList);
           treeNodes.add(parent);
       }
       return treeNodes;
    }

}
