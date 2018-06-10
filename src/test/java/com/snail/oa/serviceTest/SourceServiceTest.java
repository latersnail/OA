package com.snail.oa.serviceTest;

import com.snail.oa.entity.Source;
import com.snail.oa.service.ISourceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/21.
 */
public class SourceServiceTest extends baseServiceTest{
    @Autowired
    private ISourceService sourceService;
    @Test
    public void demo1(){
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("name","管理");
        List<Source> sourceList =
        sourceService.findSourceByPage(1,10,paraMap).getList();
        System.out.println(sourceList.size());
    }

}
