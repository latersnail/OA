package com.snail.oa.service.impl;

import com.snail.oa.entity.FormProcess;
import com.snail.oa.mapper.FormProcessMapper;
import com.snail.oa.service.IFormProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fangjiang on 2018/4/3.
 */
@Service
public class FormProcessServiceImpl implements IFormProcessService {

    @Autowired
    private FormProcessMapper formProcessMapper;

    public Integer insertFormProcess(FormProcess formProcess) {
        return formProcessMapper.insertFormProcess(formProcess);
    }

    public FormProcess findByProcessId(String processId) {
        return formProcessMapper.findByProcessId(processId);
    }

    public List<FormProcess> findByFormId(List<String> list) {
        return formProcessMapper.findByFormId(list);
    }

    public Integer deleteByProcessId(List<String> list) {
        return formProcessMapper.deleteByProcessId(list);
    }

    public Integer deleteByFormId(List<String> list) {
        return formProcessMapper.deleteByFormId(list);
    }

    public Integer updateFormId(FormProcess formProcess) {
        return formProcessMapper.updateFormId(formProcess);
    }
}
