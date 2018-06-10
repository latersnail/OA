package com.snail.oa.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.User;
import com.snail.oa.mapper.UserMapper;
import com.snail.oa.service.IUserService;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/3/23.
 */
@Service
public class UserServiceImpl implements IUserService {

    private SqlSessionTemplate sqlSessionTemplate;
    private User user = new User();
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    public UserServiceImpl(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
    @Autowired
    private UserMapper userMapper;
    public User login() {
        if(user==null)logger.error("登录参数传输失败  类名:"+IUserService.class.getName());
        user = getModel();
       // UserMapper userMapper = sqlSessionTemplate.getMapper(UserMapper.class);
        user = userMapper.login(user.getName(),user.getPassword());
        return user;
    }

    public User getUserInfoById(String userId) {
        return userMapper.getUserInfoById(userId);
    }

    public Integer updateUserInfo() {
        return userMapper.updateUserInfo(getModel());
    }

    public Integer insertUser() {
        return  userMapper.insertUser(getModel());
    }

    public PageInfo<User> getUserForPage(int pageNum,int pageSize,Map<String,String> paraMap) {
        PageHelper.startPage(pageNum,pageSize);
        List<User> userList = userMapper.getUserList(paraMap);
        PageInfo<User> pageInfo = new PageInfo<User>(userList);
        return pageInfo;
    }

    /**
    *@description 方法已经过时
    *@author  fangjiang
    *@deprecated
    *@date 2018/4/21 17:22
    */

    public PageInfo<User> getUserForPageByLoginName(int pageNum,int pageSize,String loginName) {
//        PageHelper.startPage(pageNum,pageSize);
//        PageInfo<User> pageInfo =
//                new PageInfo<User>(userMapper.getUserList());
        return null;
    }

    public User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    public List<User> findUserByActorName(String actorName) {
        return userMapper.findUserByActorName(actorName);
    }

    public List<User> findUserByOrgName(String orgName) {
        return userMapper.findUserByOrgName(orgName);
    }

    public Integer updatePwd(Map<String, String> paraMap) {
        return userMapper.updatePwd(paraMap);
    }

    public Integer updateStatus(Map<String, String> parameterMap) {
        return userMapper.updateStatus(parameterMap);
    }

    public Integer deleteUser(List<String> list) {
        return userMapper.deleteUser(list);
    }

    public void setModel(User user) {
        this.user = user;
    }

    public User getModel(){
        return this.user;
    }
}
