package com.example.HiBuddy.global.response.exception.handler;

import com.example.HiBuddy.domain.oauth.dto.CustomOAuth2User;
import com.example.HiBuddy.domain.oauth.jwt.JwtUtil;
import com.example.HiBuddy.domain.oauth.jwt.refreshtoken.RefreshToken;
import com.example.HiBuddy.domain.oauth.jwt.refreshtoken.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class UsersSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        String accessToken = jwtUtil.createJwt("access_token", username,60*60*60L);
        String refreshToken = jwtUtil.createJwt("refresh_token", username,60*60*60L);
        System.out.println("access token: "+accessToken);
        System.out.println("refresh token: "+refreshToken);

        //Refresh 토큰 저장
        addRefreshToken(username, refreshToken, 86400000L);

        // front-end에서 요청한 accessToken -> response Header / refreshToken -> response cookie
        response.setHeader("access_token",accessToken);
        response.addCookie(createCookie("refresh_token",refreshToken));
        response.setStatus(HttpStatus.OK.value());
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
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
