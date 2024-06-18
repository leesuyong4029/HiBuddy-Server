package com.example.HiBuddy.global.response.exception.handler;

import com.example.HiBuddy.global.response.code.BaseErrorCode;
import com.example.HiBuddy.global.response.exception.GeneralException;

public class TestsHandler extends GeneralException {
    public TestsHandler(BaseErrorCode errorCode) { super(errorCode);}
}
