package com.example.HiBuddy.domain.oauth.jwt;

import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.domain.user.UsersRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UsersRepository usersRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = parseBearerToken(request);
            if(token==null){ // null이 나오는 경우: Authorization이 없거나 Bearer 형식이 아닐 경우에 null을 받아옴
                filterChain.doFilter(request,response); return;
            }
            String userEmail = jwtProvider.validate(token); // token을 검증 -> 검증이 무효하면 not null
            if(userEmail==null){
                filterChain.doFilter(request,response); return;
            }

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // 빈 context 만들어줌
            AbstractAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userEmail, null); // Security Context에 token 넣음(비밀 번호 = null)
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

        }catch (Exception e){
            e.printStackTrace();
        }
        filterChain.doFilter(request,response); // 다음 필터로 넘어가도록
    }
    private String parseBearerToken(HttpServletRequest request){ // request로부터 헤더에 있는 Authorization 가져옴
        String authorization = request.getHeader("Authorization");
        boolean hasAuthorization = StringUtils.hasText(authorization);
        if(!hasAuthorization) return null;

        boolean isBearer = authorization.startsWith("Bearer ");
        if(!isBearer) return null;

        String token = authorization.substring(7);
        return token;
    }
}
