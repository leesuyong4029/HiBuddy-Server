package com.example.HiBuddy.global.response.exception.handler;

import com.example.HiBuddy.global.response.code.BaseErrorCode;
import com.example.HiBuddy.global.response.exception.GeneralException;

public class ScrabsHandler extends GeneralException {
    public ScrabsHandler(BaseErrorCode errorCode){
        super(errorCode);
    }
}
