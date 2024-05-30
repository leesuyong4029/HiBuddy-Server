package com.example.HiBuddy.global.response.exception.handler;

import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.global.response.code.BaseErrorCode;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


public class UsersHandler extends GeneralException {
    public UsersHandler(BaseErrorCode errorCode) {super(errorCode);}

}
