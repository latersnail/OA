package com.snail.oa.service;

import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.Organization;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/3/27.
 */
public interface IOrganizationService {

    PageInfo<Organization> findOrganizationByPage(int pageNum,int pageSize,Map<String,String> paraMap);

    Organization findOrganizationById(String id);

    Integer insertOrganization(Organization organization);

    Integer updateOrganization(Organization organization);

    Integer deleteOrganization(List<String> list);

    List<Organization> findOrganization(String name);

    List<Organization> findOrgByActorName(String actorName);

    Integer saveOrUpdate(Organization organization);
}
