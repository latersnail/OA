package com.snail.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.SearchViewParam;
import com.snail.oa.entity.User;
import com.snail.oa.service.impl.UserServiceImpl;
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
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/3/23.
 */
@Controller
@RequestMapping("user")
public class UserController {

    private PrintWriter out = null;
    //用户服务接口
    private  UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }
    //创建日志对象
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @ResponseBody
    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        User user = jsonObject.toJavaObject(User.class);
        userService.setModel(user);
        user = userService.login();
        String status = "";
        if(user!=null){
            if("注销".equals(user.getStatus())){
                status = "-200";
            }
            else if("锁定".equals(user.getStatus())){
                status = "-100";
            }
            else if("正常".equals(user.getStatus())){
                status = "1";
                //登录成功 将用户信息存入session中
                request.getSession().setAttribute("user",user);
            }else {
                status = "-300";
            }

        }else {
           status = "0";
        }
        return status;
    }

    /**
    *@description 用户退出系统注销session
    *@author  fangjiang
    *@date 2018/3/25 15:33
    */
    @ResponseBody
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        if(session.getAttribute("user")!=null){
            session.removeAttribute("user");
            session.invalidate();
        }
        return "success";
    }

    /**
    *@description 个人信息模块中的基本信息查询
    *@author  fangjiang
    *@date 2018/3/25 15:48
    */

    @ResponseBody
    @RequestMapping("/getUserInfo")
    public User getUserInfo(HttpSession session){
        User user = userService.getCurrentUser(session);
        user = userService.getUserInfoById(user.getId());
        return user;
    }

    /**
    *@description 用户信息更改
    *@author  fangjiang
    *@date 2018/3/26 13:44
    */

    @ResponseBody
    @RequestMapping("/updateUserInfo")
    public String updateUserInfo(HttpServletRequest request,HttpServletResponse response,
     HttpSession session){
        if(session.getAttribute("user")==null){
            return "-200";//权限不足
        }
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        User user = jsonObject != null ? jsonObject.toJavaObject(User.class) : null;
        userService.setModel(user);
        Integer integer = userService.updateUserInfo();
        if (integer>0){
            return "200";//更改成功
        }else {
            return "0";//更改失败
        }
    }

    /**
    *@description 加载用户分页信息
    *@author  fangjiang
    *@date 2018/3/26 13:45
    */
    @ResponseBody
    @RequestMapping("/findUserByPage")
    public PageUtil<User> findUserByPage(HttpServletRequest request,
                                         HttpServletResponse response, HttpSession session){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam  searchViewParam=
                jsonObject != null ? jsonObject.toJavaObject(SearchViewParam.class) : null;
        String loginName = searchViewParam.condition;
        if(loginName==null||"".equals(loginName.trim())){
           loginName = null;
        }
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("loginName",loginName);
        PageInfo<User> pageInfo = userService.getUserForPage(searchViewParam.pageNumber,searchViewParam.rowsCount,paraMap);
        logger.info("正在分页查询用户对象");
        return new PageUtil<User>(pageInfo);
    }

    /**
    *@description 根据登录用户加载用户分页信息
    *@author  fangjiang
    *@date 2018/3/26 14:04
    */
    public PageUtil<User> findUserByCondition(HttpServletRequest request,
                                              HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        SearchViewParam  searchViewParam=
                jsonObject != null ? jsonObject.toJavaObject(SearchViewParam.class) : null;
        PageInfo<User> pageInfo = userService.getUserForPageByLoginName(searchViewParam.pageNumber,
                searchViewParam.rowsCount,searchViewParam.condition);
        return new PageUtil<User>(pageInfo);
    }

    /**
    *@description 添加用户
    *@author  fangjiang
    *@date 2018/4/6 20:16
    */

    @ResponseBody
    @RequestMapping("/saveOrUpdate")
    public String saveOrUpdate(HttpServletRequest request,HttpServletResponse response){
      JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
      User user = jsonObject.toJavaObject(User.class);
      userService.setModel(user);
        int result = -1;
      if(user.getId()!=null&&!user.getId().trim().equals("")){
          result = userService.updateUserInfo();
      }else {
          result = userService.insertUser();
      }

      if(result>0){
          return "success";
      }
      return "fail";
    }

    /**
    *@description 用户密码修改
    *@author  fangjiang
    *@date 2018/4/13 15:18
    */

    @ResponseBody
    @RequestMapping("/updatePwd")
    public String updatePwd(HttpServletRequest request,HttpServletResponse response,HttpSession session){
      String result = "";
       try {
           User user = userService.getCurrentUser(session);
           JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
           String password = jsonObject.getString("password");
           Map<String,String> paraMap = new HashMap<String,String>();
           paraMap.put("id",user.getId());
           paraMap.put("password",password);
           userService.updatePwd(paraMap);
           result = "success";
       }catch (Exception e){
         result = "fail";
         logger.error("修改密码时出错",e);
       }finally {
           return result;
       }
    }

    /**
    *@description 更改用户状态
    *@author  fangjiang
    *@date 2018/4/21 11:02
    */

    @ResponseBody
    @RequestMapping("/changeStatus")
    public String changeStatus(HttpServletRequest request,HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        User user = jsonObject != null ? jsonObject.toJavaObject(User.class) : null;
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("status",user.getStatus());
        paraMap.put("id",user.getId());
        int result = userService.updateStatus(paraMap);
        if(result>0){
            return "success";
        }
        return "fail";
    }

    /**
    *@description 设置用户角色
    *@author  fangjiang
    *@date 2018/4/21 11:13
    */

    @ResponseBody
    @RequestMapping("/setActor")
    public String setActor(HttpServletRequest request,HttpServletResponse response){
        JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
        User user = jsonObject != null ? jsonObject.toJavaObject(User.class) : null;
        userService.setModel(user);
        int result = userService.updateUserInfo();
        if(result>0){
            return "success";
        }
        return "fail";
    }

    /**
    *@description 删除用户
    *@author  fangjiang
    *@date 2018/4/21 17:49
    */

    @ResponseBody
    @RequestMapping("/deleteUser")
    public String deleteUser(HttpServletRequest request,HttpServletResponse response){
        List<String> list = ControlTool.getJSONArray(request,response);
        Integer result = userService.deleteUser(list);
        if(result>0){
            return "success";
        }else {
            return "fail";
        }
    }

    /**
    *@description 根据用户ID获取用户信息
    *@author  fangjiang
    *@date 2018/4/23 10:54
    */
    @ResponseBody
    @RequestMapping("/getUserById")
    public User getUserById(HttpServletRequest request,HttpServletResponse response){
       JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
       String userId = jsonObject.getString("userId");
       return userService.getUserInfoById(userId);
    }


}
