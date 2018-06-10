package com.snail.oa.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.Organization;
import com.snail.oa.mapper.OrganizationMapper;
import com.snail.oa.service.IOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/3/27.
 */
@Service
public class OrganizationServiceImpl implements IOrganizationService{

    @Autowired
    private OrganizationMapper organizationMapper;

    public PageInfo<Organization> findOrganizationByPage(int pageNum,int pageSize,Map<String,String> paraMap) {
        PageHelper.startPage(pageNum,pageSize);
        List<Organization> list = organizationMapper.findOrganizationByPage(paraMap);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public Organization findOrganizationById(String id) {
        return  organizationMapper.findOrganizationById(id);
    }

    public Integer insertOrganization(Organization organization) {
        return organizationMapper.insertOrganization(organization);
    }

    public Integer updateOrganization(Organization organization) {
        return organizationMapper.updateOrganization(organization);
    }

    public Integer deleteOrganization(List<String> list) {
        return organizationMapper.deleteOrganization(list);
    }

    public List<Organization> findOrganization(String name) {
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("name",name);
        return organizationMapper.findOrganizationByPage(paraMap);
    }

    public List<Organization> findOrgByActorName(String actorName) {
        return organizationMapper.findOrgByActorName(actorName);
    }

    public Integer saveOrUpdate(Organization organization) {
        if(organization.getId()!=null&&!"".equals(organization.getId().trim())){
            return organizationMapper.updateOrganization(organization);
        }
        return organizationMapper.insertOrganization(organization);
    }
}
