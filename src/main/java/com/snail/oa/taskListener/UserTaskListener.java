package com.snail.oa.taskListener;

import com.snail.oa.service.IUserService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
*@description 动态人员分配
*@author  fangjiang
*@date 2018/4/7 21:20
*/
@Component
public class UserTaskListener implements TaskListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Expression actorName;
    private Expression orgName;

    @Autowired
    private IUserService userService;
    public void notify(DelegateTask delegateTask) {
//        String assignees = delegateTask.getVariable("gotoNext").toString();
//        if(assignees!=null){
//            //设置当前办理人员
//            String[] assigneeArr = assignees.split(",");
//            if(assigneeArr.length>1){
//                delegateTask.addCandidateUsers(Arrays.asList(assigneeArr));
//                logger.debug("当前任务受理人"+assigneeArr.toString());
//            }else if (assigneeArr.length==1){
//                delegateTask.setAssignee(assignees);
//                logger.debug("当前任务受理人"+assignees);
//            }
//        }
        if(orgName!=null){
            //通过组织结构名称获取代签收人
//            List<User> userList =
//                    userService.findUserByOrgName(orgName.getValue(delegateTask).toString());
//            List<String> userIdList = new ArrayList<String>();
//            for(User user : userList){
//                userIdList.add(user.getId());
//            }
//            //delegateTask.addCandidateUsers(userIdList);
//             delegateTask.setVariable("candidateUsers",userList);
//            logger.debug("当前任务签收列表有: "+userIdList.toString());
        }
    }

    public Expression getActorName() {
        return actorName;
    }

    public void setActorName(Expression actorName) {
        this.actorName = actorName;
    }

    public Expression getOrgName() {
        return orgName;
    }

    public void setOrgName(Expression orgName) {
        this.orgName = orgName;
    }
}
