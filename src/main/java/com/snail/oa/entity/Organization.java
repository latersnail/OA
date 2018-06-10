package com.snail.oa.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fangjiang on 2018/3/26.
 */
public class Organization implements Serializable {
    private String id;
    private String name;
    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Organization(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Organization() {
    }
}
