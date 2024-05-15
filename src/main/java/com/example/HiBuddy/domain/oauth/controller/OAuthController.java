package com.example.HiBuddy.domain.oauth.controller;

import com.example.HiBuddy.domain.oauth.dto.TokenDto;
import com.example.HiBuddy.domain.oauth.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api.hibuddy.shop/v1/auth")
public class OAuthController {
    private final OAuthService oAuthService;

    @SneakyThrows
    @GetMapping("/{provider}")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable String provider,
            HttpServletResponse response
    ) {
        System.out.println(1);
        String redirectUrl = oAuthService.getAuthCodeRequestUrl(provider);
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/login/{provider}")
    public ResponseEntity<Void> login(@PathVariable String provider, @RequestParam String code) { // 인가 코드
        System.out.println(3);
        TokenDto tokenDto = oAuthService.login(provider, code);
        System.out.println(4);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenDto.getAccessToken());

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", tokenDto.getRefreshToken())
                .httpOnly(true)
                // .secure(true) // HTTPS에서만 쿠키 전송
                .maxAge(7 * 24 * 60 * 60)
                .path("/") // 쿠키가 유효한 경로
                .build();
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        return ResponseEntity.ok().headers(headers).build();
    }

}
