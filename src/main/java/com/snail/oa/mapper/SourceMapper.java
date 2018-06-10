package com.snail.oa.mapper;

import com.snail.oa.entity.Source;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/11.
 */
@Repository
public interface SourceMapper {

   Integer insertSource(Source source);

   Integer updateSource(Source source);

   List<Source> findSourceByPage(Map<String,String> paraMap);

   Source findSourceById(String id);

   Integer deleteSource(List<String> list);

}
