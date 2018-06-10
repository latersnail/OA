package com.snail.oa.service;

import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.ProcessInfo;

import java.util.List;

/**
 * Created by fangjiang on 2018/3/28.
 */
public interface IProcessInfoService {
    Integer insertProcessInfo(ProcessInfo processInfo);

    PageInfo<ProcessInfo> findProcessInfoList(int pageNum, int pageSize,String processInfoName);

    Integer updateProcessInfo(ProcessInfo processInfo);

    Integer deleteProcessInfo(List<String> list);

    Integer saveOrUpdate(ProcessInfo processInfo);
}
