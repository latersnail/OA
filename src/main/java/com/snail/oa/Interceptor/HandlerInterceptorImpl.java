package com.snail.oa.Interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
*@description 请求拦截器处理
*@author  fangjiang
*@date 2018/4/4 22:42
*/

public class HandlerInterceptorImpl implements HandlerInterceptor{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //配置不需要拦截的请求
    private List<String> excludeUrls;

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    //记录开始时间
    private long startTime;

    //记录结束时间
    private long endTime;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        startTime = System.currentTimeMillis();
        logger.debug("请求开始  "+request.getRequestURI());
        //过滤静态资源的请求
        if(handler instanceof ResourceHttpRequestHandler){
            return true;
        }
        //获取请求的URI
        String requestURI = request.getRequestURI();
        //如果请求的URI中在配置不需要拦截的请求里面则放开
        for(String url : excludeUrls){
            if(requestURI.endsWith(url)){
                return true;
            }
        }
        Object user = request.getSession().getAttribute("user");
        if(user==null){
            String basePath = request.getScheme()+"://"+
                    request.getServerName()+":"+request.getServerPort()+"/";
            response.sendRedirect(basePath+"login.jsp");
            return false;
        }
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        endTime = System.currentTimeMillis();
        logger.debug("请求结束  "+request.getRequestURI()+"    请求响应时间"+(endTime-startTime)+"毫秒");
    }
}
