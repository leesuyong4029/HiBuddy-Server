package com.example.HiBuddy.global.response.exception.handler;

import com.example.HiBuddy.global.response.code.BaseErrorCode;
import com.example.HiBuddy.global.response.exception.GeneralException;

public class ScriptsHandler extends GeneralException {
    public ScriptsHandler(BaseErrorCode errorCode) { super(errorCode);}

}

