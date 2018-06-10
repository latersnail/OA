package com.snail.oa.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.snail.oa.entity.SearchViewParam;
import com.snail.oa.exception.SnailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ControlTool<T> {
	static Logger logger = LoggerFactory.getLogger(ControlTool.class);
	/*根据页面传递的常规JSON参数，获取*/
	public static SearchViewParam GetViewParam(JSONObject param)
	{ 	 
		SearchViewParam model=new SearchViewParam();
		try {
			model.condition=param.getString("condition");
			model.pageNumber=param.getInteger("pageNumber");
			model.rowsCount=param.getInteger("rowsCount");
			return model;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return model;
		}
	}


	public static SearchViewParam GetViewParamNP(JSONObject param)
	{
		SearchViewParam model=new SearchViewParam();
		try {
			model.condition=param.getString("condition");
			return model;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return model;
		}
	}

	/*获取页面传递JSON操作doType的数据*/
	public static int GetDoType(JSONObject param)
	{  
		try {
			return param.getInteger("doType");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	public static PrintWriter getResponseOut(HttpServletResponse response){
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			logger.error("获取响应输出对象错误");
		}
		return out;
	}

	/*
        * 获取客户端请求参数，客户端的数据必须为JSON对象，返回JSON对象
        * */
	public static JSONObject GetRequestJSON(HttpServletRequest request, HttpServletResponse response)
	{
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		try
		{
			String json= JSONAndObject.GetPostData(request.getInputStream(), request.getContentLength(), "utf-8");
			return JSONAndObject.GetJSONObject(json);
		}catch(IOException e)
		{
			return null;
		}

	}

	public static List<String> getJSONArray(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		try
		{
			String json= JSONAndObject.GetPostData(request.getInputStream(), request.getContentLength(), "utf-8");
			return JSONArray.parseArray(json,String.class);
		}catch(IOException e)
		{
			return null;
		}
	}

	public List<T> jsonToList(HttpServletRequest request, HttpServletResponse response,Class<T> clazz) throws SnailException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		try {
			String json = JSONAndObject.GetPostData(request.getInputStream(),request.getContentLength(),"utf-8");
			return  JSONArray.parseArray(json,clazz);
		} catch (IOException e) {
			throw new SnailException("读取request中的输入流错误",e);
		}

	}

	public static String formatData(Date date,String regex){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(regex);
			return  simpleDateFormat.format(date);
	}

	/**
	*@description 响应图片请求
	*@author  fangjiang
	*@date 2018/4/21 21:33
	*/

	public static void responseImage(HttpServletResponse response, InputStream in) throws IOException {
		byte[] bytes = readInputStreamToByteArray(in);
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-Type","image/png");
		response.getOutputStream().write(bytes);
		response.flushBuffer();
	}


	/**
	*@description 读取输入流到二进制数组中
	*@author  fangjiang
	*@date 2018/4/21 21:37
	*/

	public static byte[] readInputStreamToByteArray(InputStream in){
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			int len = -1;
			while((len=in.read(buffer))!=-1){
				out.write(buffer,0,len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
				try {
					out.close();
					if(in!=null)in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		return out.toByteArray();
	}

}
