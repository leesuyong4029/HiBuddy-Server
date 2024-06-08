package com.example.HiBuddy.domain.oauth.jwt;

import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = request.getHeader("Authorization");

        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = accessToken.substring(7); // Remove "Bearer " prefix

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            setUnauthorizedResponse(response, ErrorStatus.EXPIRED_JWT_EXCEPTION);
            return;
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!"Authorization".equals(category)) {
            setUnauthorizedResponse(response, ErrorStatus.INVALID_ACCESS_TOKEN);
            return;
        }

        String username = jwtUtil.getUsername(accessToken);
        Users users = new Users();
        users.setUsername(username);

        Authentication authToken = new UsernamePasswordAuthenticationToken(users, null, users.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private void setUnauthorizedResponse(HttpServletResponse response, ErrorStatus errorStatus) throws IOException {
        ApiResponse<Object> apiResponse = ApiResponse.onFailure(
                errorStatus.getCode(),
                errorStatus.getMessage(),
                null
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}




