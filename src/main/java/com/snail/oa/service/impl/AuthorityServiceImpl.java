package com.snail.oa.service.impl;

import com.snail.oa.entity.Authority;
import com.snail.oa.entity.SourceGroup;
import com.snail.oa.mapper.AuthorityMapper;
import com.snail.oa.service.IAuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by fangjiang on 2018/4/14.
 */
@Service
public class AuthorityServiceImpl implements IAuthorityService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AuthorityMapper authorityMapper;

    /**
    *@description 方法已经过时 已被saveOrUpdate替代
    *@author  fangjiang
    *@deprecated
    *@date 2018/4/14 21:20
    */

    public Integer batchInsertAuthority(List<Authority> list) {
        return authorityMapper.batchInsertAuthority(list);
    }

    public List<SourceGroup> findAuthorityByActorId(String actorId) {
        return authorityMapper.findAuthorityByActorId(actorId);
    }

    public Integer deleteSourceByActorId(String actorId) {
        return authorityMapper.deleteSourceByActorId(actorId);
    }

    public Integer saveOrUpdate(List<Authority> list){
        if (CollectionUtils.isEmpty(list)){
            return 0;
        }
        try {
            //先清除之前角色存有的资源
            authorityMapper.deleteSourceByActorId(list.get(0).getActorId());
        }catch (Exception e){
            logger.error("角色操作的权限资源删除失败",e);
        }

        return authorityMapper.batchInsertAuthority(list);
    }

    /**
    *@description 通过用户id获取用户所能操作的资源
    *@author  fangjiang
    *@date 2018/4/14 21:19
    */

    public List<SourceGroup> findAuthorityByUserId(String userId) {
        return authorityMapper.findAuthorityByUserId(userId);
    }
}
