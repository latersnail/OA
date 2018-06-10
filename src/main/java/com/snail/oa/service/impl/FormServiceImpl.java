package com.snail.oa.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.Form;
import com.snail.oa.mapper.FormMapper;
import com.snail.oa.service.IFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/2.
 */
@Service
public class FormServiceImpl implements IFormService{
    @Autowired
    private FormMapper formMapper;
    public Form findFormById(String id) {
        return formMapper.findFormById(id);
    }

    public PageInfo<Form> findFormByPage(int pageNum, int pageSize, Map<String,String> paraMap) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<Form>(formMapper.findFormByPage(paraMap));
    }

    public Integer deleteForm(List<String> list) {
        return formMapper.deleteForm(list);
    }

    public Integer saveOrUpdate(Form form) {
        if(form.getId()!=null&&!"".equals(form.getId().trim())){
            return formMapper.updateForm(form);
        }
        return formMapper.insertForm(form);
    }

    public List<Form> findFormList(String formName) {
        Map<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("formName",formName);
        return formMapper.findFormByPage(paraMap);
    }
}
