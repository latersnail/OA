package com.snail.oa.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.SourceGroup;
import com.snail.oa.mapper.SourceGroupMapper;
import com.snail.oa.service.ISourceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/11.
 */
@Service
public class SourceGroupServiceImpl implements ISourceGroupService {

    @Autowired
    private SourceGroupMapper sourceGroupMapper;

    public Integer saveOrUpdate(SourceGroup sourceGroup) {
        if(sourceGroup.getId()!=null&&!"".equals(sourceGroup.getId().trim())){
            return sourceGroupMapper.updateSourceGroup(sourceGroup);
        }
        return sourceGroupMapper.insertSourceGroup(sourceGroup);
    }

    public Integer deleteSourceGroup(List<String> list) {
        return sourceGroupMapper.deleteSourceGroup(list);
    }

    public SourceGroup findSourceGroupById(String id) {
        return sourceGroupMapper.findSourceGroupById(id);
    }

    public PageInfo<SourceGroup> findSourceGroupByPage(int pageNum,int pageSize,String name) {
        PageHelper.startPage(pageNum,pageSize);
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("name",name);
        return  new PageInfo<SourceGroup>(sourceGroupMapper.findSourceGroupByPage(paraMap));
    }

    public List<SourceGroup> findSourceByGroup() {
        return sourceGroupMapper.findSourceByGroup();
    }

    public List<SourceGroup> findSourceGroupByList() {
        return sourceGroupMapper.findSourceGroupByPage(null);
    }
}
