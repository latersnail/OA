import com.snail.oa.service.impl.TestService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by fangjiang on 2018/3/24.
 */
public class ApplicationTest {
    @Test
    public  void getApplication(){
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("application-context.xml");
        TestService userService = applicationContext.getBean("testService", TestService.class);
        System.out.println(userService);

    }
}
