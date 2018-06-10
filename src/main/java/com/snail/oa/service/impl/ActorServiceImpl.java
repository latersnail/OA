package com.snail.oa.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.Actor;
import com.snail.oa.mapper.ActorMapper;
import com.snail.oa.service.IActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by fangjiang on 2018/3/27.
 */
@Service
public class ActorServiceImpl implements IActorService {


    @Autowired
    private ActorMapper actorMapper;

    public Integer deleteActor(List<String> list) {

        return actorMapper.deleteActor(list);
    }

    public Integer insertActor(Actor actor) {

        return actorMapper.insertActor(actor);
    }

    public PageInfo<Actor> findActorByPage(int pageNum,int pageSize, Map<String,String> paraMap) {
        PageHelper.startPage(pageNum,pageSize);
        List<Actor> list = actorMapper.findActorByPage(paraMap);
        PageInfo<Actor> pageInfo = new PageInfo<Actor>(list);
        return pageInfo;
    }

    public Integer updateActor(Actor actor) {
        return actorMapper.updateActor(actor);
    }

    public Actor findActorById(String id) {
        return actorMapper.findActorById(id);
    }

    public List<Actor> findActorForList( Map<String,String> paraMap) {
        return actorMapper.findActorByPage(paraMap);
    }

    public Actor findActorByUserId(String userId) {
        return null;
    }

    public Integer saveOrUpdate(Actor actor) {
        if(actor.getId()!=null&&!"".equals(actor.getId().trim())){
           return actorMapper.updateActor(actor);
        }
        return actorMapper.insertActor(actor);
    }
}
