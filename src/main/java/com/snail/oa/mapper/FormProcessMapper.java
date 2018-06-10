package com.snail.oa.mapper;

import com.snail.oa.entity.FormProcess;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fangjiang on 2018/4/3.
 */

@Repository
public interface FormProcessMapper {

    Integer insertFormProcess(FormProcess formProcess);

    FormProcess findByProcessId(String processId);

    List<FormProcess> findByFormId(List<String> list);

    Integer deleteByProcessId(List<String> list);

    Integer deleteByFormId(List<String> list);

    Integer updateFormId(FormProcess formProcess);
}
