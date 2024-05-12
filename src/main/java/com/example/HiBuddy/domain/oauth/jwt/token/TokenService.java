package com.example.HiBuddy.domain.oauth.jwt.token;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {
    String reissue(HttpServletRequest request, HttpServletResponse response);
}
