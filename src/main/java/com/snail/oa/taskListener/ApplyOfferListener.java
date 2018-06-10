package com.snail.oa.taskListener;

import com.snail.oa.entity.User;
import com.snail.oa.service.IUserService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangjiang on 2018/4/8.
 */
@Component("applyOfferListener")
public class ApplyOfferListener implements TaskListener{

    private Expression actorName;
    private Expression orgName;
    @Autowired
    private IUserService userService;

    public void notify(DelegateTask delegateTask) {
        if(actorName!=null){
            String roleName = actorName.getValue(delegateTask).toString();
            List<User> userList = userService.findUserByActorName(roleName);
            List<String> ids = new ArrayList<String>();
            for(User user:userList){
                ids.add(user.getId());
            }
            delegateTask.addCandidateUsers(ids);

            //delegateTask.setVariable();
        }

        if(orgName!=null){
//            String organizationName = orgName.getValue(delegateTask).toString();
//            List<String> ids = userService.findUserByOrgName(organizationName);
//            delegateTask.addCandidateUsers(ids);
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
