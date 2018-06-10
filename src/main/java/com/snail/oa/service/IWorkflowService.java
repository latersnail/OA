package com.snail.oa.service;

import java.io.InputStream;

/**
 * Created by fangjiang on 2018/4/21.
 */
public interface IWorkflowService {

    /**
    *@description 流程图查看
    *@author  fangjiang
    *@date 2018/4/21 21:02
    */
    InputStream getProcessImage(String processDefinitionId);

    /**
    *@description 查看流程定义文件
    *@author  fangjiang
    *@date 2018/4/21 21:04
    */

    InputStream getProcessXml(String processDefinitionId);


}
