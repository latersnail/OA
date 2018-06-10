package com.snail.oa.util;

public class LoggerUtil {
  
   public static String error(String reason) {
	   StackTraceElement ste = new Throwable().getStackTrace()[1];
	   return "发生异常的类:"+ste.getFileName()+",第"+ste.getLineNumber()+"行/r/n"
               +"原因可能是:"+reason;
   }
}
