package com.snail.oa.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fangjiang on 2018/4/22.
 */
public interface BaseController {

    @ResponseBody
    @RequestMapping("/saveOrUpdate")
    String saveOrUpdate(HttpServletRequest request, HttpServletResponse response);
}
