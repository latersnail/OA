package com.snail.oa.service;

import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.Actor;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/3/27.
 */
public interface IActorService {
    /**
    *@description 删除角色
    *@author  fangjiang
    *@date 2018/3/27 15:45
    */

    Integer deleteActor(List<String> list);

    /**
    *@description 新增角色
    *@author  fangjiang
    *@date 2018/3/27 15:46
    */

    Integer insertActor(Actor actor);

    /**
    *@description 查询角色分页数据
    *@author  fangjiang
    *@date 2018/3/27 15:48
    */

    PageInfo<Actor> findActorByPage(int pageNum, int pageSize, Map<String,String> paraMap);

    /**
    *@description 更改角色信息
    *@author  fangjiang
    *@date 2018/3/27 15:49
    */

    Integer updateActor(Actor actor);

    /**
    *@description 根据ID查找角色信息
    *@author  fangjiang
    *@date 2018/3/27 15:50
    */

    Actor findActorById(String id);

    /**
    *@description 组织结构列表
    *@author  fangjiang
    *@date 2018/4/6 21:20
    */
    List<Actor> findActorForList( Map<String,String> paraMap);

    /**
    *@description 通过用户id获取角色信息
    *@author  fangjiang
    *@date 2018/4/14 21:08
    */

    Actor findActorByUserId(String userId);

    /**
    *@description 保存与更改角色信息
    *@author  fangjiang
    *@date 2018/4/22 10:22
    */
    Integer saveOrUpdate(Actor actor);



}
