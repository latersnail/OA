package com.snail.oa.service;

import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.User;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/3/23.
 */
public interface IUserService {
    /**
     *@description 用户登录
     *@author  fangjiang
     *@date 2018/3/23 13:14
     */
    User login();

    /**
     *@description 通过id 查询用户信息
     *@author  fangjiang
     *@date 2018/3/23 13:16
     */

    User getUserInfoById(String userId);

    /**
     *@description 修改用户信息
     *@author  fangjiang
     *@date 2018/3/23 13:18
     */

    Integer updateUserInfo();

    /**
     *@description 插入一条用户信息
     *@author  fangjiang
     *@date 2018/3/23 13:19
     */

    Integer insertUser();

    /**
     *@description 分页查询用户信息
     *@author  fangjiang
     *@date 2018/3/23 13:33
     */

    PageInfo<User> getUserForPage(int pageNum,int pageSize,Map<String,String> paraMap);

    /**
    *@description 设置操作数据
    *@author  fangjiang
    *@date 2018/3/23 14:42
    */

    void setModel(User user);

    /**
    *@description 根据用户登录名分页查询用户数据
    *@param pageNum
    *@param pageSize
    *@param loginName
    *@author  fangjiang
    *@date 2018/3/26 12:21
    */

    PageInfo<User> getUserForPageByLoginName(int pageNum,int pageSize,String loginName);

    /**
    *@description 通过session得到当前用户信息
    *@author  fangjiang
    *@date 2018/4/6 11:08
    */

    User getCurrentUser(HttpSession session);

    /**
     *@description 根据角色名称获取用户信息
     *@author  fangjiang
     *@date 2018/4/6 15:30
     */

    List<User> findUserByActorName(String actorName);

    /**
     *@description 根据部门名称获取用户ID
     *@author  fangjiang
     *@date 2018/4/8 14:58
     */

    List<User> findUserByOrgName(String orgName);


    /**
     *@description 修改登陆密码
     *@author  fangjiang
     *@date 2018/4/8 14:58
     */
    Integer updatePwd(Map<String,String> paraMap);

    /**
    *@description 更改用户状态
    *@author  fangjiang
    *@date 2018/4/21 11:09
    */

    Integer updateStatus(Map<String,String> parameterMap);

    /**
    *@description 删除用户
    *@author  fangjiang
    *@date 2018/4/21 17:51
    */

    Integer deleteUser(List<String> list);


}
