package com.example.HiBuddy.global.response.exception.handler;

import com.example.HiBuddy.domain.postLike.PostLikesRepository;
import com.example.HiBuddy.global.response.code.BaseErrorCode;
import com.example.HiBuddy.global.response.exception.GeneralException;

public class PostLikesHandler extends GeneralException {
    public PostLikesHandler(BaseErrorCode errorCode) { super(errorCode);}
}
