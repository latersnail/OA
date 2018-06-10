package com.snail.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.snail.oa.entity.Authority;
import com.snail.oa.entity.SourceGroup;
import com.snail.oa.entity.User;
import com.snail.oa.exception.SnailException;
import com.snail.oa.service.IAuthorityService;
import com.snail.oa.service.IUserService;
import com.snail.oa.util.ControlTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by fangjiang on 2018/4/14.
 */
@Controller
@RequestMapping("authority")
public class AuthorityController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IAuthorityService authorityService;
    @Autowired
    private IUserService userService;
    /**
    *@description 角色权限的添加和修改
    *@author  fangjiang
    *@date 2018/4/14 16:37
    */

    @ResponseBody
    @RequestMapping("/saveOrUpdate")
    public String saveOrUpdate(HttpServletRequest request, HttpServletResponse response) throws SnailException {
        List<Authority> list =
            new ControlTool<Authority>().jsonToList(request,response,Authority.class);
       Integer result = authorityService.saveOrUpdate(list);
       if(result>0){
           return "success";
       }
        return "fail";
    }

    /**
    *@description 首页页面加载时 初始化资源列表
    *@author  fangjiang
    *@date 2018/4/14 16:42
    */
    @ResponseBody
    @RequestMapping("/initSource")
    public List<SourceGroup> initSource(HttpSession session){
     User user = userService.getCurrentUser(session);
     return authorityService.findAuthorityByUserId(user.getId());
    }

    /**
    *@description 通过角色ID获取资源树数据
    *@author  fangjiang
    *@date 2018/4/14 16:50
    */
    @ResponseBody
    @RequestMapping("/getSourceByActorId")
    public List<SourceGroup> getSourceByActorId(HttpServletRequest request,HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        String actorId = jsonObject != null ? jsonObject.getString("actorId") : null;
        List<SourceGroup> list = authorityService.findAuthorityByActorId(actorId);
        return   list;
    }



}
