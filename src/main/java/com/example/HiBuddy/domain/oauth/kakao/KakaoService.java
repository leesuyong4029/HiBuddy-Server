package com.example.HiBuddy.domain.oauth.kakao;


import com.example.HiBuddy.domain.oauth.jwt.JwtProvider;
import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.domain.user.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Service
public class KakaoService {
    @Autowired
    private UsersService usersService;
    @Autowired
    private KakaoProvider kakaoProvider;
    @Autowired
    private final JwtProvider jwtProvider;

    public KakaoService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public KakaoTokenResponse getAccessToken(String code){
        return kakaoProvider.getKakaoOAuthToken(code);
    }

    public KakaoProfile getUserInfo(KakaoTokenResponse accessToken){
        String jwtAccessToken = "";
        String jwtRefreshToken = "";
        KakaoProfile kakaoProfile = kakaoProvider.getKakaoProfile(accessToken);

        Users kakaoUsers = Users.builder()
                .email(kakaoProfile.getKakao_account().getEmail())
                .oauth("kakao")
                .build();

        Users originUsers = usersService.findByEmail(kakaoUsers.getEmail());

        if (originUsers.getId() == null) {
            System.out.println("새 사용자 로그인 처리");
            // 새 사용자 등록 로그 또는 처리
            usersService.join(kakaoUsers);
            jwtAccessToken = jwtProvider.createAccessToken(originUsers.getEmail());
            jwtRefreshToken = jwtProvider.createRefreshToken();
            originUsers.updateRefreshToken(jwtRefreshToken);
            System.out.println("Access Token: "+jwtAccessToken);
            System.out.println("Refresh Token: "+jwtRefreshToken);
        } else {
            // 기존 사용자 로그인 처리
            System.out.println("기존 사용자 로그인 처리");
            jwtAccessToken = jwtProvider.createAccessToken(originUsers.getEmail());
            jwtRefreshToken = jwtProvider.createRefreshToken();
            originUsers.updateRefreshToken(jwtRefreshToken);
            System.out.println("Access Token: "+jwtAccessToken);
            System.out.println("Refresh Token: "+jwtRefreshToken);
        }
        return kakaoProfile;
    }

}
