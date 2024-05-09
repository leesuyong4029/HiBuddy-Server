package com.example.HiBuddy.domain.oauth.kakao;


import com.example.HiBuddy.domain.oauth.jwt.JwtProvider;
import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.domain.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private final UsersRepository usersRepository;
    private final JwtProvider jwtProvider;
    private final KakaoProvider kakaoProvider;

    public KakaoTokenResponse getAccessToken(String code){
        return kakaoProvider.getKakaoOAuthToken(code);
    }

    public TokenDto kakaoLogin(KakaoTokenResponse accessToken){
        String jwtAccessToken = "";
        String jwtRefreshToken = "";
        KakaoProfile kakaoProfile = kakaoProvider.getKakaoProfile(accessToken);

        Users kakaoUsers = Users.builder()
                .email(kakaoProfile.getKakao_account().getEmail())
                .oauth("kakao")
                .build();

        Users originUsers = usersRepository.findByEmail(kakaoUsers.getEmail()).orElse(null);

        if (originUsers == null) {
            System.out.println("새 사용자 로그인 처리");
            // 새 사용자 등록 로그 또는 처리
            jwtAccessToken = jwtProvider.createAccessToken(kakaoUsers.getEmail());
            jwtRefreshToken = jwtProvider.createRefreshToken();
            kakaoUsers.updateRefreshToken(jwtRefreshToken);
            usersRepository.save(kakaoUsers);
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
        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

}