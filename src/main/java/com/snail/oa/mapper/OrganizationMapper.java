package com.snail.oa.mapper;

import com.snail.oa.entity.Organization;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/3/27.
 */

@Repository
public interface OrganizationMapper {

    List<Organization> findOrganizationByPage(Map<String,String> paraMap);

    Organization findOrganizationById(String id);

    Integer insertOrganization(Organization organization);

    Integer updateOrganization(Organization organization);

    Integer deleteOrganization(List<String> list);

    List<Organization> findOrgByActorName(String actorName);
}
