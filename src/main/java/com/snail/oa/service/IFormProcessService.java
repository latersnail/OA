package com.snail.oa.service;

import com.snail.oa.entity.FormProcess;

import java.util.List;

/**
 * Created by fangjiang on 2018/4/3.
 */
public interface IFormProcessService {

    Integer insertFormProcess(FormProcess formProcess);

    FormProcess findByProcessId(String processId);

    List<FormProcess> findByFormId(List<String> list);

    Integer deleteByProcessId(List<String> list);

    Integer deleteByFormId(List<String> list);

    Integer updateFormId(FormProcess formProcess);
}
