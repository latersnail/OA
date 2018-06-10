package com.snail.oa.mapper;

import com.snail.oa.entity.RecvDoc;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/4/1.
 */
@Repository
public interface RecvDocMapper {

    Integer saveRecvDoc(RecvDoc recvDoc);

    Integer updateRecvDoc(RecvDoc recvDoc);

    List<RecvDoc> getDraftDoc(String userId);

    RecvDoc getRecvDocById(String id);

    List<RecvDoc> findTodoTask(Map<String,String> paraMap);

    List<RecvDoc> findTaskByProcessInstanceId(Map<String,String> paraMap);

    List<RecvDoc> findProcessTask(Map<String,String> paraMap);

    List<RecvDoc> findProcessedTask(Map<String,String> paraMap);

    Integer updateDocStatus(Map<String,String> paraMap);

    List<RecvDoc> findAbandonDoc(Map<String,String> paraMap);

    Integer deleteRecvDoc(List<String> list);
}
