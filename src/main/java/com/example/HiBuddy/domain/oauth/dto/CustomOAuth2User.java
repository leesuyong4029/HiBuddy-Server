package com.example.HiBuddy.domain.oauth.dto;

import com.example.HiBuddy.domain.user.UsersRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final UsersRequestDto.UsersOAuthDto usersOAuthDto; // email. username
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // no role
        return null;
    }

    @Override
    public String getName() {
        return usersOAuthDto.getEmail();
    }

    public String getUsername(){
        return usersOAuthDto.getUsername();
    }
}
