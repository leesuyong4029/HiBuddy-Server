package com.example.HiBuddy.domain.oauth.kakao;

import com.example.HiBuddy.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hibuddy/v1/auth/kakao")
public class KakaoController {
    private final KakaoService kakaoService;
    @GetMapping(value = "/login")
    @Operation(summary = "카카오 로그인", description = "카카오유저 정보가 담긴 token을 KakaoRequestDto로 받아 유저 확인 후 TokenDto반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    public ApiResponse<TokenDto> login(@RequestParam("code") String code) {
        KakaoTokenResponse access_token = kakaoService.getAccessToken(code);
        return ApiResponse.onSuccess(kakaoService.kakaoLogin(access_token));
    }
}

