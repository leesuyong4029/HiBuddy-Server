package com.example.HiBuddy.global.response.exception.handler;

import com.example.HiBuddy.global.response.code.BaseErrorCode;
import com.example.HiBuddy.global.response.exception.GeneralException;

public class PostsHandler extends GeneralException {
    public PostsHandler(BaseErrorCode errorCode){
        super(errorCode);
    }
}
