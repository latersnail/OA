package com.snail.oa.serviceTest;

import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.Actor;
import com.snail.oa.service.IActorService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/21.
 */
public class ActorServiceTest extends baseServiceTest {

    @Autowired
    private IActorService actorService;
    @Test
    public void demo1(){
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("name","管理");
        PageInfo<Actor> actorPage = actorService.findActorByPage(1, 10, paraMap);
        System.out.println(actorPage.getList().size());
    }
}
