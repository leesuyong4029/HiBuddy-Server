package com.example.HiBuddy.domain.oauth.kakao;


import com.example.HiBuddy.domain.user.User;
import com.example.HiBuddy.domain.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("hibuddy/v1/auth/kakao")
public class KakaoController {
    @Value("${security.oauth2.client.registration.kakao.clientId}")
    private String kakaoClientId;

    @Value("${security.oauth2.client.registration.kakao.redirectUri}")
    private String kakaoRedirectUri;

    @Autowired
    private UserService userService;

    @Autowired
    private KakaoProvider kakaoProvider;


    @GetMapping("/login")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code,  HttpServletResponse response) throws IOException {
        KakaoTokenResponse oauthToken = kakaoProvider.getKakaoOAuthToken(code);
        KakaoProfile kakaoProfile = kakaoProvider.getKakaoProfile(oauthToken);

        User kakaoUser = User.builder()
                .email(kakaoProfile.getKakao_account().getEmail())
                .oauth("kakao")
                .build();

        User originUser = userService.findByEmail(kakaoUser.getEmail());

        if (originUser.getId() == null) {
            userService.join(kakaoUser);
            // 새 사용자 등록 로그 또는 처리
        } else {
            // 기존 사용자 로그인 처리
            System.out.println("기존 사용자 로그인 처리");
            // 기존 사용자 로그인 처리


        }
        // 필요한 경우, 사용자에게 반환할 응답 객체 생성 및 반환
        return ResponseEntity.ok("Login successful");
    }
}
