package com.snail.oa.taskListener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * Created by fangjiang on 2018/4/7.
 */
public class VacationListner implements TaskListener{

    public void notify(DelegateTask delegateTask) {
        //UserTask
      delegateTask.getProcessInstanceId();
    }
}
