package com.example.HiBuddy.domain.oauth.jwt.token;

import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.global.response.code.resultCode.SuccessStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {
    ApiResponse<SuccessStatus> reissue(HttpServletRequest request, HttpServletResponse response);
}

