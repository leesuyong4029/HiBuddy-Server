package com.example.HiBuddy.global.response.exception.handler;

import com.example.HiBuddy.global.response.code.BaseErrorCode;
import com.example.HiBuddy.global.response.exception.GeneralException;

public class CommentsHandler extends GeneralException {
    public CommentsHandler(BaseErrorCode errorCode) {super(errorCode);}
}
