package com.snail.oa.mapper;

import com.snail.oa.entity.Authority;
import com.snail.oa.entity.SourceGroup;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fangjiang on 2018/4/14.
 */
@Repository
public interface AuthorityMapper {

    Integer batchInsertAuthority(List<Authority> list);

    List<SourceGroup> findAuthorityByActorId(String actorId);

    Integer deleteSourceByActorId(String actorId);

    List<SourceGroup> findAuthorityByUserId(String userId);

}
