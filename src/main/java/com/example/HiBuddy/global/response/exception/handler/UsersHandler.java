package com.example.HiBuddy.global.response.exception.handler;

import com.example.HiBuddy.global.response.code.BaseErrorCode;
import com.example.HiBuddy.global.response.exception.GeneralException;

public class UsersHandler extends GeneralException {
    public UsersHandler(BaseErrorCode errorCode) {super(errorCode);}

}
