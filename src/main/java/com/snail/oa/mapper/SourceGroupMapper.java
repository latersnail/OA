package com.snail.oa.mapper;

import com.snail.oa.entity.SourceGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/11.
 */
@Repository
public interface SourceGroupMapper {

    Integer insertSourceGroup(SourceGroup sourceGroup);

    Integer updateSourceGroup(SourceGroup sourceGroup);

    List<SourceGroup> findSourceGroupByPage(Map<String,String> paraMap);

    SourceGroup findSourceGroupById(String id);

    Integer deleteSourceGroup(List<String> list);

    List<SourceGroup> findSourceByGroup();

}
