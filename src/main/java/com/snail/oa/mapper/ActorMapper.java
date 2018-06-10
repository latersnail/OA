package com.snail.oa.mapper;

import com.snail.oa.entity.Actor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/3/27.
 */
@Repository
public interface ActorMapper {
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

    List<Actor> findActorByPage(Map<String,String> paraMap);

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
}
