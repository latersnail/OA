package com.snail.oa.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**************************************************************************
 * 实现JSON格式的数据读取操作
 * 
 **************************************************************************/
public class JSONAndObject {
	
	/*获取页面传来的JSON参数，转为JSON对象*/
	public static String GetPostData(InputStream in, int size, String charset) {
		if (in != null && size > 0) {
			byte[] buf = new byte[size];
			try {
				in.read(buf);
				if (charset == null || charset.length() == 0)
					return new String(buf,"utf-8");
				else {
					System.out.println("客户端数据："+new String(buf, charset));
					return new String(buf, charset);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	} 
	/*将JSON数据转换成JavaBean对象*/
	public static <T> T JSONObjectToJavaBean(JSONObject source,Class<T> javaBean)
	{
		if (source==null) return null;
		Method[] beanMethods = javaBean.getMethods();//获取指定对象所有的JavaBean方法  
        T tempBean = null;  
        try {  
            tempBean = javaBean.newInstance();  
        } catch (Exception e){  
        	e.printStackTrace();
            return null;
        }  
        for (Method method : beanMethods)  
        { 
        	String field = method.getName();  
        	if (field.indexOf("set")<0) continue;//没有get方法的过
        	
        	String oldField=field.substring(3); 
            field = field.substring(3); 
            
            field = field.substring(0,1).toLowerCase() + field.substring(1); 
            if (source.getString(field)==null&&"".equals(source.getString(field)))
				continue;
			else {
				try {
					if (source.get(field) instanceof JSONObject)
					{	
					   method.invoke(tempBean, new Object[] {JSONObjectToJavaBean(source.getJSONObject(field), method.getParameterTypes()[0])});
						 
					} else if (source.get(field) instanceof JSONArray)
					{ 	
						List<Object> lTemp=new java.util.ArrayList<Object>();
						JSONArray jArray=source.getJSONArray(field);
						for(int i=0;i<jArray.size();i++)
						{
							ParameterizedType pt=null;
							try {
								pt = (ParameterizedType)tempBean.getClass().getMethod("get"+oldField).getGenericReturnType();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
							lTemp.add(JSONObjectToJavaBean(jArray.getJSONObject(i),(Class)pt.getActualTypeArguments()[0]));
						}
						method.invoke(tempBean, new Object[] {lTemp});//
					}
					else {
						method.invoke(tempBean, new Object[] {source.get(field)});
					} 
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					System.out.println("异常属性【"+field+"】");
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					System.out.println("异常JSON属性【"+field+"】");
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
        	  
        }
		return tempBean;
	}

	public static JSONObject GetJSONObject(String str) {
		if (str == null || str.trim().length() == 0) {
			return null;
		}
		JSONObject jsonObject = null;
		try {

			jsonObject = JSON.parseObject(str);
		} catch (JSONException e) {
			e.printStackTrace(System.err);
		}
		return jsonObject;
	}
}
