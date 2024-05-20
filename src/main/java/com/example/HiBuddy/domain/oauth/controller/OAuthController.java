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
@RequestMapping("/v1/auth")
public class OAuthController {
    private final OAuthService oAuthService;

    @SneakyThrows
    @GetMapping("/{provider}")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @PathVariable String provider,
            HttpServletResponse response
    ) {
        String redirectUrl = oAuthService.getAuthCodeRequestUrl(provider);
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login/{provider}")
    public ResponseEntity<Void> login(@PathVariable String provider, @RequestParam String code) { // 인가 코드
        TokenDto tokenDto = oAuthService.login(provider, code);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenDto.getAccessToken());
        headers.add("Access-Control-Expose-Headers", "Authorization");

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(false) // Set to true in production
                .sameSite("None")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok().headers(headers).build();
    }
}


