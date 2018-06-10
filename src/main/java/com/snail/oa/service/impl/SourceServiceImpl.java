package com.snail.oa.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.Source;
import com.snail.oa.mapper.SourceMapper;
import com.snail.oa.service.ISourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/11.
 */
@Service
public class SourceServiceImpl implements ISourceService {

    @Autowired
    private SourceMapper sourceMapper;

    public Integer saveOrUpdateSource(Source source) {
        if(source.getId()!=null&&!"".equals(source.getId().trim())){
            return sourceMapper.updateSource(source);
        }
        return sourceMapper.insertSource(source);
    }

    public Integer deleteSource(List<String> list) {
        return sourceMapper.deleteSource(list);
    }

    public Source findSourceById(String id) {
        return sourceMapper.findSourceById(id);
    }

    public PageInfo<Source> findSourceByPage(int pageNum,int pageSize,Map<String,String> paraMap) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<Source>(sourceMapper.findSourceByPage(paraMap));
    }
}
