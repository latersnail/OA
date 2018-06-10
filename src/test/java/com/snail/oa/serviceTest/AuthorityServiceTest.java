package com.snail.oa.serviceTest;

import com.snail.oa.entity.Authority;
import com.snail.oa.entity.SourceGroup;
import com.snail.oa.service.IAuthorityService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangjiang on 2018/4/14.
 */

public class AuthorityServiceTest extends baseServiceTest{
    @Autowired
    private IAuthorityService authorityService;

    @Test
    public void batchInsertAuthorityTest(){
       List<Authority> list = new ArrayList<Authority>();
       Authority authority1 = new Authority("2","3");
       Authority authority2 = new Authority("2","4");
       Authority authority3 = new Authority("2","5");
       Authority authority4 = new Authority("2","6");
       Authority authority5 = new Authority("2","7");
       list.add(authority1);
       list.add(authority2);
       list.add(authority3);
       list.add(authority4);
       list.add(authority5);
       Integer result = authorityService.batchInsertAuthority(list);
        System.out.println("成功插入"+result+"条数据");
    }

    @Test
    public void findAuthorityByActorId(){
        String actorId = "2";
//        List<> list = authorityService.findAuthorityByActorId(actorId);
//        for(Authority authority:list){
//            System.out.println(authority.getSourceId());
//        }
    }

    @Test
    public void delteSourceByActorId(){
        String actorId="2";
        Integer result = authorityService.deleteSourceByActorId(actorId);
        System.out.println("#########################成功删除"+result+"条数据");
    }

    @Test
    public void findAuthorityByUserId(){
        String userId = "bc4899493d3311e8ad8baf1f8a571566";
        List<SourceGroup> sourceGroupList = authorityService.findAuthorityByUserId(userId);
        for(SourceGroup sourceGroup:sourceGroupList){
            System.out.println(sourceGroup.getName());
        }
    }
}
