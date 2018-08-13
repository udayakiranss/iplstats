package com.example.ipl.iplstats.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IPLStatException extends  Exception {

    private String errorCode;

    private String errorMessage;

    public IPLStatException(String errorCode, String errorMessage){
       this.errorCode = errorCode;
       this.errorMessage = errorMessage;
    }

    public IPLStatException(String errorCode, Throwable cause){
        super(cause);
        this.errorCode = errorCode;

    }

}
