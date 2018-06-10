package com.snail.oa.service;

import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.Source;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/11.
 */
public interface ISourceService {

    Integer saveOrUpdateSource(Source source);

    Integer deleteSource(List<String> list);

    Source findSourceById(String id);

    PageInfo<Source> findSourceByPage(int pageNum,int pageSize,Map<String,String> paraMap);

}
