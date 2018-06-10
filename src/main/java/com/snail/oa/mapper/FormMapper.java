package com.snail.oa.mapper;

import com.snail.oa.entity.Form;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/2.
 */
@Repository
public interface FormMapper {

    Form findFormById(String id);

    List<Form> findFormByPage(Map<String,String> paraMap);

    Integer deleteForm(List<String> list);

    Integer updateForm(Form form);

    Integer insertForm(Form form);

}
