package com.snail.oa.service;

import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.Form;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/2.
 */
public interface IFormService {
    Form findFormById(String id);

    PageInfo<Form> findFormByPage(int pageNum,int pageSize,Map<String,String> paraMap);

    Integer deleteForm(List<String> list);

    Integer saveOrUpdate(Form form);

    List<Form> findFormList(String formName);
}
