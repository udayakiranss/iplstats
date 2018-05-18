package com.example.ipl.iplstats.exception;

public class IPLStatException extends  Exception {

    private String errorCode;

    private String errorMessage;

    public IPLStatException(){
        super();
    }

    public IPLStatException(String errorCode, String errorMessage){
       this.errorCode = errorCode;
       this.errorMessage = errorMessage;
    }

}
