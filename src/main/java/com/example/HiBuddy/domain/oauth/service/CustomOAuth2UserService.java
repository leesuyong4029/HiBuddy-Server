package com.example.HiBuddy.domain.oauth.service;

import com.example.HiBuddy.domain.oauth.dto.CustomOAuth2User;
import com.example.HiBuddy.domain.oauth.dto.GoogleUserInfo;
import com.example.HiBuddy.domain.oauth.dto.KakaoUserInfo;
import com.example.HiBuddy.domain.oauth.dto.OAuth2UserInfo;
import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.domain.user.UsersRepository;
import com.example.HiBuddy.domain.user.UsersRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UsersRepository usersRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2Response = null;
        switch (registrationId){
            case "kakao":
                oAuth2Response = new KakaoUserInfo(oAuth2User.getAttributes());
                break;
            case "google":
                oAuth2Response = new GoogleUserInfo(oAuth2User.getAttributes());
                break;
            default:
                return null;
        }
        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+"_"+oAuth2Response.getProviderId(); // custom

        Users existData = usersRepository.findByUsername(username).orElse(null);
        if (existData==null){
            System.out.println("신규 유저 로그인");
            Users users = new Users();
            users.setUsername(username);
            users.setEmail(oAuth2Response.getEmail());
            usersRepository.save(users);

            UsersRequestDto.UsersOAuthDto usersOAuthDto = new UsersRequestDto.UsersOAuthDto();
            usersOAuthDto.setUsername(username);
            usersOAuthDto.setEmail(oAuth2Response.getEmail());
            return new CustomOAuth2User(usersOAuthDto);
        }
        else{
            System.out.println("기존 유저 로그인");
            existData.setUsername(username);
            existData.setEmail(oAuth2Response.getEmail());
            usersRepository.save(existData);

            UsersRequestDto.UsersOAuthDto usersOAuthDto = new UsersRequestDto.UsersOAuthDto();
            usersOAuthDto.setUsername(existData.getUsername());;
            usersOAuthDto.setEmail(existData.getEmail());
            return new CustomOAuth2User(usersOAuthDto);

        }
    }
}

