package com.example.HiBuddy.global.response.exception.handler;

import com.example.HiBuddy.global.response.code.BaseErrorCode;
import com.example.HiBuddy.global.response.exception.GeneralException;

public class ImagesHandler extends GeneralException {
    public ImagesHandler(BaseErrorCode errorCode){
        super(errorCode);
    }
}
