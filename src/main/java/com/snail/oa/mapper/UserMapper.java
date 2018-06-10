package com.snail.oa.mapper;

import com.snail.oa.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/3/23.
 */
@Repository
public interface UserMapper {
    /**
    *@description 用户登录
    * @param name
    * @param password
    *@author  fangjiang
    *@date 2018/3/23 13:14
    */
    User login(@Param("name") String name, @Param("password") String password);

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

    Integer updateUserInfo(User user);

    /**
    *@description 插入一条用户信息
    *@author  fangjiang
    *@date 2018/3/23 13:19
    */

    Integer insertUser(User user);

    /**
    *@description 查询用户信息
    *@author  fangjiang
    *@date 2018/3/23 13:33
    */

    List<User> getUserList(Map<String,String> paraMap);

    Integer deleteUser(List<String> list);

    Integer updatePwd(Map<String,String> parameterMap);

    Integer updateStatus(Map<String,String> parameterMap);

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


}
