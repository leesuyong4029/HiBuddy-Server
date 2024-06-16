package com.example.HiBuddy.global.response.exception.handler;

import com.example.HiBuddy.global.response.code.BaseErrorCode;
import com.example.HiBuddy.global.response.exception.GeneralException;

public class GeneralHandler extends GeneralException {
    public GeneralHandler (BaseErrorCode errorCode) { super(errorCode); }
}
