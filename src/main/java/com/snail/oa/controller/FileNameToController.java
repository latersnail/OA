package com.snail.oa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fangjiang on 2018/3/23.
 */
@Controller
public class FileNameToController {

    @RequestMapping(value = "/fileNameToController")
    public String fileNameToController(HttpServletRequest request){

        return request.getParameter("fileName");
    }
}
