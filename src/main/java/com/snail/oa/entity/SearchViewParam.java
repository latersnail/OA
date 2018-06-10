package com.snail.oa.entity;
/*********************************
 * 类名：视图层查询常规控制参数持久模型
 * 描述：定义视图层常规操作持久数据模型。
 * 作者：wph
 * 创建日期：2017-5-15 10:50:5
 * 版本：1.0.0.0
 * *******************************/
public class SearchViewParam {
   public String condition="";//单值查询条件
   public int doType=-1;//操作类别
   public int pageNumber=-1;//页面索引
   public int rowsCount=-1;//每页显示行数
   public String type;//访问类型
}
