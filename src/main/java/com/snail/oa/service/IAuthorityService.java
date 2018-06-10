package com.snail.oa.service;

import com.snail.oa.entity.Authority;
import com.snail.oa.entity.SourceGroup;
import com.snail.oa.exception.SnailException;

import java.util.List;

/**
 * Created by fangjiang on 2018/4/14.
 */
public interface IAuthorityService {

    Integer batchInsertAuthority(List<Authority> list);

    List<SourceGroup> findAuthorityByActorId(String actorId);

    Integer deleteSourceByActorId(String actorId);

    Integer saveOrUpdate(List<Authority> list) throws SnailException;

    List<SourceGroup> findAuthorityByUserId(String userId);
}
