package com.snail.oa.util;

import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by fangjiang on 2018/4/14.
 */
public class CollectionUtilTest {
    @Test
    public void demo1(){
        List<String> list = Collections.emptyList();
        if(CollectionUtils.isEmpty(list)){
            System.out.println("..........");
        }
    }
}
