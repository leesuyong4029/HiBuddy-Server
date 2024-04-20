package com.example.HiBuddy.domain.oauth.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("hibuddy/v1/auth/kakao")
public class KakaoController {
    private final KakaoService kakaoService;
    @GetMapping(value = "/login")
    public KakaoProfile login(@RequestParam("code") String code) {
        KakaoTokenResponse access_token = kakaoService.getAccessToken(code);
        return kakaoService.getUserInfo(access_token);
    }
}
