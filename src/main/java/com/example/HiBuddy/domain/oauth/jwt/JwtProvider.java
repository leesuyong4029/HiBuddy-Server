package com.example.HiBuddy.domain.oauth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${jwt.secret_key}")
    private String secretKey;

    // 토큰 유효시간 1시간
    private final long tokenValidTime = 60 * 60 * 1000L;
    // refresh token 유효시간 7일
    private final long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L;

    public String createAccessToken(String email){ // jwt를 생성하는 메서드
        Date now = new Date();
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)); // 시크릿 키 만들기
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256) //
                .setSubject(email)
                .setIssuedAt(now) // 발행시간 설정
                .setExpiration(new Date(now.getTime() + tokenValidTime))  // 토큰의 만료시간 현재로부터 1시간으로 설정
                .compact();
    }

    public String validate(String jwt){
        String subject = null;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            subject = claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return subject;
    }

    //Refresh Token 생성
    public String createRefreshToken(){
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 토큰에서 회원 정보(이메일) 추출
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져오는 메서드 "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveAccessToken(HttpServletRequest request) {
        return request.getHeader("X-ACCESS-TOKEN");
    }
    public String resolveRefreshToken(HttpServletRequest request) {
        return request.getHeader("X-REFRESH-TOKEN");
    }

    //     access token의 만료일자 가져오기
    public Long getExpiration(String accessToken){
        Date expiration = Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(accessToken).getBody().getExpiration();
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    // Access Token의 유효성 + 만료일자 확인
    public boolean validateAccessToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(accessToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(refreshToken);
            return claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
