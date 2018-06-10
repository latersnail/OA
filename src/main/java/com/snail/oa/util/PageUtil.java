package com.snail.oa.util;


import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
*@description  分页工具类(只适合easyui的分页插件)
*@author  fangjiang
*@date 2018/4/12 21:34
*/

public class PageUtil<T> implements Serializable{

    private long total;

    private List<T> rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public PageUtil(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public PageUtil() {
    }

    public PageUtil(PageInfo<T> pageInfo){
        this.total = pageInfo.getTotal();
        this.rows = pageInfo.getList();
    }
}
