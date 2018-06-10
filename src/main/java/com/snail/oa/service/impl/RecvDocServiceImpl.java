package com.snail.oa.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.RecvDoc;
import com.snail.oa.mapper.RecvDocMapper;
import com.snail.oa.service.IRecvDocService;
import com.snail.oa.util.PageUtil;
import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/1.
 */
@Service
public class RecvDocServiceImpl implements IRecvDocService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RepositoryService repositoryService;

    @Autowired RecvDocMapper recvDocMapper;

    public Integer saveOrUpdate(RecvDoc recvDoc) {
        if(recvDoc.getId()==null||"".equals(recvDoc.getId().trim())){
            return recvDocMapper.saveRecvDoc(recvDoc);
        }
        return recvDocMapper.updateRecvDoc(recvDoc);
    }

    /**
    *@description 获取草稿文件
    *@author  fangjiang
    *@date 2018/4/7 15:52
    */

    public PageInfo<RecvDoc> getDraftDoc(String userId,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<RecvDoc>(recvDocMapper.getDraftDoc(userId));
    }
    public RecvDoc getRecvDocById(String id) {
        return recvDocMapper.getRecvDocById(id);
    }

    public Integer gotoNext(RecvDoc recvDoc) {
        return null;
    }

    /**
    *@description 获取待办任务
    *@author  fangjiang
    *@date 2018/4/7 15:51
    */

    public PageInfo<RecvDoc> findTodoTask(int pageNum,int pageSize,Map<String,String> paraMap) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<RecvDoc>(recvDocMapper.findTodoTask(paraMap));
    }

    /**
    *@description 获取代签收任务列表
    *@author  fangjiang
    *@date 2018/4/9 16:19
    */

    public PageInfo<RecvDoc> findSignTask(int pageNum, int pageSize, Map<String, String> paraMap) {
        logger.debug("查询参数"+paraMap.values().toArray().toString());
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<RecvDoc>(recvDocMapper.findTaskByProcessInstanceId(paraMap));
    }

    /**
    *@description
    *@author  fangjiang
    *@date 2018/4/9 16:27
    */

    public RecvDoc findTaskByProcessInstanceId(Map<String, String> paraMap) {
        logger.debug("查询参数"+paraMap.values().toArray().toString());
        List<RecvDoc> recvDocList = recvDocMapper.findTaskByProcessInstanceId(paraMap);
        RecvDoc recvDoc = null;
        if(recvDocList.size()>0){
            recvDoc = recvDocList.get(0);
        }
        else if(recvDocList.size()>1){
            logger.debug("数据超出预料的结果 期望查询一条数据,却查询出了"+recvDocList.size()+"条");
        }else {
            logger.debug("没有查询到数据");
        }
        return recvDoc;
    }

    /**
    *@description 获取在办任务
    *@author  fangjiang
    *@date 2018/4/12 19:55
    */

    public PageInfo<RecvDoc> findProcessTask(int pageNum, int pageSize, Map<String, String> paraMap) {
        PageHelper.startPage(pageNum,pageSize);
        List<RecvDoc> recvDocList = recvDocMapper.findProcessTask(paraMap);
        return new PageInfo<RecvDoc>(recvDocList);
    }

    /**
    *@description  获取办结任务
    *@author  fangjiang
    *@date 2018/4/12 19:55
    */

    public PageInfo<RecvDoc> findProcessedTask(int pageNum, int pageSize, Map<String, String> paraMap) {
        PageHelper.startPage(pageNum,pageSize);
        List<RecvDoc> recvDocList = recvDocMapper.findProcessedTask(paraMap);
        return new PageInfo<RecvDoc>(recvDocList);
    }

    public Integer abandonDoc(Map<String, String> paraMap) {
        paraMap.put("status","2");
        return recvDocMapper.updateDocStatus(paraMap);
    }

    public Integer recoverDoc(Map<String, String> paraMap) {
        paraMap.put("status","1");
        return recvDocMapper.updateDocStatus(paraMap);
    }

    public PageUtil<RecvDoc> findAbandonDoc(int pageNum, int pageSize, Map<String,String> paraMap) {
        PageHelper.startPage(pageNum,pageSize);
        PageInfo<RecvDoc> pageInfo = new PageInfo<RecvDoc>(recvDocMapper.findAbandonDoc(paraMap));
        return new PageUtil<RecvDoc>(pageInfo);
    }

    public Integer deleteRecvDoc(List<String> list) {
        return recvDocMapper.deleteRecvDoc(list);
    }

}
