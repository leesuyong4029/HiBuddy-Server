package com.example.HiBuddy.domain.oauth.jwt.token;

import com.example.HiBuddy.domain.oauth.jwt.JwtUtil;
import com.example.HiBuddy.domain.oauth.jwt.refreshtoken.RefreshToken;
import com.example.HiBuddy.domain.oauth.jwt.refreshtoken.RefreshTokenRepository;
import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.domain.user.UsersRepository;
import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.code.resultCode.SuccessStatus;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UsersRepository usersRepository;

    @Override
    public ApiResponse<SuccessStatus> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        if (refresh == null) {
            return ApiResponse.onFailure(ErrorStatus.REFRESH_TOKEN_NOT_FOUND.getCode(), ErrorStatus.REFRESH_TOKEN_NOT_FOUND.getMessage(), null);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return ApiResponse.onFailure(ErrorStatus.EXPIRED_REFRESH_TOKEN.getCode(), ErrorStatus.EXPIRED_REFRESH_TOKEN.getMessage(), null);
        }

        String category = jwtUtil.getCategory(refresh);
        if (!"refreshToken".equals(category)) {
            return ApiResponse.onFailure(ErrorStatus.INVALID_REFRESH_TOKEN.getCode(), ErrorStatus.INVALID_REFRESH_TOKEN.getMessage(), null);
        }

        String username = jwtUtil.getUsername(refresh);
        Users user = usersRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ApiResponse.onFailure(ErrorStatus.USER_NOT_FOUND.getCode(), ErrorStatus.USER_NOT_FOUND.getMessage(), null);
        }

        String newAccess = jwtUtil.createJwt("Authorization", username, user.getId(), 86400L); // 하루
        String newRefresh = jwtUtil.createJwt("refreshToken", username, user.getId(), 604800L);


        // Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshTokenRepository.deleteByRefresh(refresh);
        addRefreshToken(username, newRefresh, 604800L);

        // Authorization 헤더 설정
        response.setHeader("Authorization", "Bearer " + newAccess);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefresh)
                .httpOnly(true)
                .maxAge(604800L)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ApiResponse.onSuccess(SuccessStatus.USER_TOKEN_REISSUE_SUCCESS);
    }

    private void addRefreshToken(String username, String refreshToken, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refresh = new RefreshToken();
        refresh.setUsername(username);
        refresh.setRefresh(refreshToken);
        refresh.setExpiration(date.toString());

        refreshTokenRepository.save(refresh);
    }
}


