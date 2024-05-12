package com.example.HiBuddy.domain.oauth.jwt.token;

import com.example.HiBuddy.domain.oauth.jwt.JwtUtil;
import com.example.HiBuddy.domain.oauth.jwt.refreshtoken.RefreshToken;
import com.example.HiBuddy.domain.oauth.jwt.refreshtoken.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        if (refresh == null) {
            return "refresh token null";
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return "refresh token expired";
        }

        String category = jwtUtil.getCategory(refresh);
        if (!"refresh_token".equals(category)) {
            return "invalid refresh token";
        }

        String username = jwtUtil.getUsername(refresh);

        String newAccess = jwtUtil.createJwt("access_token", username, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh_token", username, 86400000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshTokenRepository.deleteByRefresh(refresh);
        addRefreshToken(username, newRefresh, 86400000L);


        response.setHeader("access_token", newAccess);
        return null; // 성공 시 null 반환
    }

    private void addRefreshToken(String username, String refreshToken, Long expiredMs){
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refresh = new RefreshToken();
        refresh.setUsername(username);
        refresh.setRefresh(refreshToken);
        refresh.setExpiration(date.toString());

        refreshTokenRepository.save(refresh);
    }
}

