package com.snail.oa.exception;

/**
 * Created by fangjiang on 2018/4/9.
 */
public class SnailException extends Exception {
    public SnailException(){
    }

    public SnailException(String message){
        super(message);
    }

    public SnailException(Throwable reason){
        super(reason);
    }

    public SnailException(String message,Throwable reason){
        super(message,reason);
    }
}
