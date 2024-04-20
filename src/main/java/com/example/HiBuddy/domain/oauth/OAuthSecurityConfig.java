package com.example.HiBuddy.domain.oauth;

import com.example.HiBuddy.domain.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;

@Configuration
@EnableWebSecurity
public class OAuthSecurityConfig {
    private final UserService userService;

    public OAuthSecurityConfig(UserService userService) {
        this.userService = userService;
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/**").authenticated() // 유저 페이지는 권한이 있어야 댐
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // ADMIN 역할이 필요한 요청
                        .anyRequest().permitAll())  // 그 외의 모든 요청에 대해 접근 허용
                /*.formLogin(form -> form
                        .loginPage("/login")  // 사용자 정의 로그인 페이지 설정
                        .permitAll())  // 모든 사용자에게 로그인 페이지 접근 허용*/
                .csrf(CsrfConfigurer::disable);  // CSRF 보호 기능 비활성화

        return http.build();
    }
}
