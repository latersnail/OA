package com.snail.oa.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.ProcessInfo;
import com.snail.oa.mapper.ProcessInfoMapper;
import com.snail.oa.service.IFormProcessService;
import com.snail.oa.service.IProcessInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fangjiang on 2018/3/28.
 */

@Service
public class ProcessInfoServiceImpl implements IProcessInfoService{

    @Autowired
    private ProcessInfoMapper processInfoMapper;
    @Autowired
    private IFormProcessService formProcessService;

    public Integer insertProcessInfo(ProcessInfo processInfo) {
        return null;
    }

    public PageInfo<ProcessInfo> findProcessInfoList(int pageNum, int pageSize,String processInfoName) {
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<ProcessInfo> pageInfo =
                new PageInfo<ProcessInfo>(processInfoMapper.findProcessInfoList(processInfoName));
        return pageInfo;
    }

    public Integer updateProcessInfo(ProcessInfo processInfo) {
        return null;
    }

    public Integer deleteProcessInfo(List<String> list) {
        return formProcessService.deleteByProcessId(list);
    }

    //整合保存于更新
    public Integer saveOrUpdate(ProcessInfo processInfo) {

        if(processInfo.getId()==null||"".equals(processInfo.getId())){
            return  processInfoMapper.insertProcessInfo(processInfo);
        }else {
            return processInfoMapper.updateProcessInfo(processInfo);
        }
    }
}
