package com.snail.oa.service;

import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.SourceGroup;

import java.util.List;

/**
 * Created by fangjiang on 2018/4/11.
 */
public interface ISourceGroupService {

    Integer saveOrUpdate(SourceGroup sourceGroup);

    Integer deleteSourceGroup(List<String> list);

    SourceGroup findSourceGroupById(String id);

    PageInfo<SourceGroup> findSourceGroupByPage(int pageNum,int pageSize,String name);

    List<SourceGroup> findSourceByGroup();

    List<SourceGroup> findSourceGroupByList();

}
