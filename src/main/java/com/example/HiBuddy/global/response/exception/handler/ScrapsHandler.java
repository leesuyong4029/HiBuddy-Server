package com.example.HiBuddy.global.response.exception.handler;

import com.example.HiBuddy.global.response.code.BaseErrorCode;
import com.example.HiBuddy.global.response.exception.GeneralException;

public class ScrapsHandler extends GeneralException {
    public ScrapsHandler(BaseErrorCode errorCode) { super(errorCode);}

}
