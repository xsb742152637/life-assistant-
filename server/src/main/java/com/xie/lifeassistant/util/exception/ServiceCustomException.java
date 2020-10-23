package com.xie.lifeassistant.util.exception;

/**
 * Comment：业务自定义异常
 * Created by IntelliJ IDEA.
 * User: xie
 * Date: 2020/9/29 15:02
 */
public class ServiceCustomException extends RuntimeException {

    private  Integer customStatus;
    private String customMessage;

    private static final long serialVersionUID = 1L;

    public ServiceCustomException() {
        super();
    }

    public ServiceCustomException(Integer customStatus ,String customMessage) {
        this.customStatus=customStatus;
        this.customMessage=customMessage;
    }

    public ServiceCustomException(Integer customStatus ,String customMessage,Throwable cause) {
        super(cause);
        this.customStatus=customStatus;
        this.customMessage=customMessage;
    }


    public Integer getCustomStatus() {
        return customStatus;
    }


    public void setCustomStatus(Integer customStatus) {
        this.customStatus = customStatus;
    }


    public String getCustomMessage() {
        return customMessage;
    }


    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

}
