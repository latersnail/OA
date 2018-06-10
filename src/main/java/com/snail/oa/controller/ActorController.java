package com.snail.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.Actor;
import com.snail.oa.entity.SearchViewParam;
import com.snail.oa.entity.TreeNode;
import com.snail.oa.service.IActorService;
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
@RequestMapping("actor")
public class ActorController implements BaseController{
    @Autowired
    private IActorService actorService;

    /**
    *@description 查询角色分页数据
    *@author  fangjiang
    *@date 2018/4/21 18:52
    */

    @ResponseBody
    @RequestMapping("/findAcotrByPage")
    public PageUtil<Actor> findAcotrByPage(HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam searchViewParam=
                jsonObject != null ? jsonObject.toJavaObject(SearchViewParam.class) : null;
        String actorName = searchViewParam.condition;
        if(actorName==null||"".equals(actorName.trim())){
            actorName = null;
        }
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("actorName",actorName);
        PageInfo<Actor> pageInfo =
                actorService.findActorByPage(searchViewParam.pageNumber,searchViewParam.rowsCount,paraMap);
      return new PageUtil<Actor>(pageInfo);
    }

    @ResponseBody
    @RequestMapping("/insertActor")
    public String insertActor(HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        Actor actor = jsonObject.toJavaObject(Actor.class);
        int result = actorService.insertActor(actor);
        if(result>0){
            return "success";
        }
        return "fail";
    }

    @ResponseBody
    @RequestMapping("/updateActor")
    @Deprecated
    public String updateActor(HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        Actor actor = jsonObject.toJavaObject(Actor.class);
        int result = actorService.updateActor(actor);
        if(result>0){
            return "success";
        }
        return "fail";
    }

    @RequestMapping("/input")
    @Deprecated
    public ModelAndView input(HttpServletRequest request){
        String id = request.getParameter("id");
        ModelAndView modelAndView = new ModelAndView();
        if(id!=null){
            Actor actor = actorService.findActorById(id);
            modelAndView.addObject("actor",actor);
            modelAndView.setViewName("/actor/actor-input");
        }else {
            modelAndView.setViewName("/actor/actor-add");
        }
        return  modelAndView;
    }

    @ResponseBody
    @RequestMapping("/deleteActor")
    public String deleteActor(HttpServletRequest request, HttpServletResponse response){
        List<String> list = ControlTool.getJSONArray(request,response);
        int result = actorService.deleteActor(list);
        if(result>0){
            return "success";
        }
        return "fail";
    }

    /**
    *@description 角色列表树
    *@author  fangjiang
    *@date 2018/4/6 21:22
    */
    @ResponseBody
    @RequestMapping("/getActorTree")
    public List<TreeNode> getActorTree(){
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("actorName",null);
        List<Actor> actorList = actorService.findActorForList(paraMap);
        TreeNode parentNode = new TreeNode("","角色列表");
        List<TreeNode> childNodes = new ArrayList<TreeNode>();
        for(Actor actor : actorList){
            TreeNode childNode = new TreeNode(actor.getId(),actor.getActorName());
            childNodes.add(childNode);
        }
        parentNode.setChildren(childNodes);
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        treeNodes.add(parentNode);
        return treeNodes;
    }



    /**
    *@description 角色编辑与新增
    *@author  fangjiang
    *@date 2018/4/22 10:08
    */
//    @ResponseBody
//    @RequestMapping("/saveOrUpdate")
//    public String saveOrUpdate(HttpServletRequest request,HttpServletResponse response){
//
//    }

    public String saveOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        Actor actor = jsonObject != null ? jsonObject.toJavaObject(Actor.class) : null;
        Integer result = actorService.saveOrUpdate(actor);
        if(result>0){
            return "success";
        }
        return "fail";
    }


}
