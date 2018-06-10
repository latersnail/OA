package com.snail.oa.serviceTest;

import com.snail.oa.service.IWorkflowService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by fangjiang on 2018/4/21.
 */
public class WorkflowServiceTest extends baseServiceTest {
    @Autowired
    private IWorkflowService workflowService;
    @Test
    public void demo1(){
        workflowService.getProcessImage("bxsq:1:42508");
    }
}
