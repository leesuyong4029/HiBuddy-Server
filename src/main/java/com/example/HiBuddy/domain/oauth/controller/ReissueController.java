package com.example.HiBuddy.domain.oauth.controller;

import com.example.HiBuddy.domain.oauth.jwt.refreshtoken.RefreshTokenRepository;
import com.example.HiBuddy.domain.oauth.jwt.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String result = tokenService.reissue(request, response);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

