package com.snail.oa.service;

import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.RecvDoc;
import com.snail.oa.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/1.
 */
public interface IRecvDocService {

    Integer saveOrUpdate(RecvDoc recvDoc);

    PageInfo<RecvDoc> getDraftDoc(String userId,int pageNum,int pageSize);

    RecvDoc getRecvDocById(String id);

    Integer gotoNext(RecvDoc recvDoc);

    /**
    *@description 找到待办文件
    *@author  fangjiang
    *@date 2018/4/13 16:39
    */

    PageInfo<RecvDoc> findTodoTask(int pageNum,int pageSize,Map<String,String> paraMap);

    /**
    *@description 待签文件
    *@author  fangjiang
    *@date 2018/4/13 16:40
    */

    PageInfo<RecvDoc> findSignTask(int pageNum,int pageSize,Map<String,String> paraMap);

    RecvDoc findTaskByProcessInstanceId(Map<String,String> paraMap);

    /**
    *@description 在办文件
    *@author  fangjiang
    *@date 2018/4/13 16:39
    */

    PageInfo<RecvDoc> findProcessTask(int pageNum,int pageSize,Map<String,String> paraMap);

    /**
    *@description 办结文件
    *@author  fangjiang
    *@date 2018/4/13 16:39
    */

    PageInfo<RecvDoc> findProcessedTask(int pageNum,int pageSize,Map<String,String> paraMap);

    /**
    *@description 回收文件
    *@author  fangjiang
    *@date 2018/4/13 16:38
    */

    Integer abandonDoc(Map<String,String> paraMap);

    /**
    *@description 还原文件
    *@author  fangjiang
    *@date 2018/4/13 16:38
    */

    Integer recoverDoc(Map<String,String> paraMap);

    PageUtil<RecvDoc> findAbandonDoc(int pageNum, int pageSize, Map<String,String> paraMap);

    Integer deleteRecvDoc(List<String> list);
}
