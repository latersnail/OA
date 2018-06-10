import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by fangjiang on 2018/3/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-*.xml"})
public class springTest {

    @Autowired
    TaskService taskService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    RepositoryService repositoryService;
    @Test
    public void testListener(){
        Task task =
                 taskService.createTaskQuery().processInstanceId("37513").singleResult();
        taskService.complete(task.getId());
    }
    @Test
    public void deleteProcessDefinition(){
       List<Deployment> deployments = repositoryService.createDeploymentQuery().list();
       for(Deployment deployment: deployments){
           repositoryService.deleteDeployment(deployment.getId(),true);
       }
    }
}
