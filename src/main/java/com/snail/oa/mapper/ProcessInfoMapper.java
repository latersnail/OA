package com.snail.oa.mapper;

import com.snail.oa.entity.ProcessInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fangjiang on 2018/3/28.
 */
@Repository
public interface ProcessInfoMapper {

    Integer insertProcessInfo(ProcessInfo processInfo);

    List<ProcessInfo> findProcessInfoList(String processInfoName);

    Integer updateProcessInfo(ProcessInfo processInfo);

    Integer deleteProcessInfo(List<String> list);
}
