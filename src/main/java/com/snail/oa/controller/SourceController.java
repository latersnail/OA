package com.snail.oa.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.snail.oa.entity.SearchViewParam;
import com.snail.oa.entity.Source;
import com.snail.oa.service.ISourceService;
import com.snail.oa.util.ControlTool;
import com.snail.oa.util.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("source")
public class SourceController {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private ISourceService sourceService;

  @ResponseBody
  @RequestMapping("/saveOrUpdateSource")
  public String saveOrUpdateSource(HttpServletRequest request, HttpServletResponse response){
      JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
      Source source = jsonObject.toJavaObject(Source.class);
      Integer result = sourceService.saveOrUpdateSource(source);
      if(result>0){
          return "success";
      }
      return "fail";
  }

  @ResponseBody
  @RequestMapping("/deleteSource")
  public String deleteSource(HttpServletRequest request,HttpServletResponse response){
      List<String> list = ControlTool.getJSONArray(request, response);
      Integer result = sourceService.deleteSource(list);
      if(result>0){
          return "success";
      }
     return "fail";
  }

  @ResponseBody
  @RequestMapping("/findSourceByPage")
  public PageUtil<Source> findSourceByPage(HttpServletRequest request, HttpServletResponse response){
      JSONObject jsonObject = ControlTool.GetRequestJSON(request,response);
      SearchViewParam searchViewParam = jsonObject.toJavaObject(SearchViewParam.class);
      String name = searchViewParam.condition;
      if(name==null||"".equals(name.trim())){
          name = null;
      }
      Map<String,String> paraMap = new HashMap<String,String>();
      paraMap.put("name",name);
      PageInfo<Source> pageInfo =
                                  sourceService.findSourceByPage(searchViewParam.pageNumber,searchViewParam.rowsCount,paraMap);
      return new PageUtil<Source>(pageInfo);
  }
}
