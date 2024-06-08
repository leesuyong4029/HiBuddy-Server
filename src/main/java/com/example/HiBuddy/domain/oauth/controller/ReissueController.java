package com.example.HiBuddy.domain.oauth.controller;

import com.example.HiBuddy.domain.oauth.jwt.refreshtoken.RefreshTokenRepository;
import com.example.HiBuddy.domain.oauth.jwt.token.TokenService;
import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.global.response.code.resultCode.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class ReissueController {

    private final TokenService tokenService;

    @Operation(summary = "Reissue JWT tokens", description = "Reissues access and refresh tokens if the provided refresh token is valid.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OAUTH402", description = "Refersh Token을 찾지 못했습니다", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OAUTH403", description = "Refersh Token이 만료되었습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OAUTH404", description = "Refersh Token이 유효하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<SuccessStatus>> reissue(HttpServletRequest request, HttpServletResponse response) {
        ApiResponse<SuccessStatus> result = tokenService.reissue(request, response);
        return new ResponseEntity<>(result, result.getIsSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}

