package com.example.HiBuddy.domain.oauth.google;

import com.example.HiBuddy.domain.oauth.dto.TokenDto;
import com.example.HiBuddy.domain.oauth.jwt.JwtUtil;
import com.example.HiBuddy.domain.oauth.jwt.refreshtoken.RefreshToken;
import com.example.HiBuddy.domain.oauth.jwt.refreshtoken.RefreshTokenRepository;
import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.domain.user.UsersRepository;
import com.example.HiBuddy.domain.user.UsersRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class GoogleService {
    private final UsersRepository usersRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final GoogleProvider googleProvider;

    public GoogleTokenResponse getAccessToken(String code){
        return googleProvider.getGoogleOAuthToken(code);
    }

    public TokenDto googleLogin(GoogleTokenResponse accessToken){
        String jwtAccessToken = "";
        String jwtRefreshToken = "";
        GoogleProfile googleProfile = googleProvider.getGoogleProfile(accessToken);

        String username = "google"+"_"+googleProfile.getSub();

        Users googleUsers = Users.builder()
                .email(googleProfile.getEmail())
                .status(true)
                .username(username)
                .build();

        Users originUsers = usersRepository.findByUsername(googleUsers.getUsername()).orElse(null);

        if (originUsers == null) {
            System.out.println("새 사용자 로그인 처리");
            // 새 사용자 등록 로그 또는 처리
            jwtAccessToken = jwtUtil.createJwt("Authorization",googleUsers.getUsername(),600000L);
            jwtRefreshToken = jwtUtil.createJwt("refresh_token",googleUsers.getUsername(),86400000L);
            addRefreshToken(username,jwtRefreshToken,86400000L);
            usersRepository.save(googleUsers);
            System.out.println("Access Token: "+jwtAccessToken);
            System.out.println("Refresh Token: "+jwtRefreshToken);
        } else {
            // 기존 사용자 로그인 처리
            System.out.println("기존 사용자 로그인 처리");
            jwtAccessToken = jwtUtil.createJwt("Authorization",googleUsers.getUsername(),600000L);
            jwtRefreshToken = jwtUtil.createJwt("refresh_token",googleUsers.getUsername(),86400000L);
            addRefreshToken(username,jwtRefreshToken,86400000L);;
            System.out.println("Access Token: "+jwtAccessToken);
            System.out.println("Refresh Token: "+jwtRefreshToken);
        }
        return TokenDto.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
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
