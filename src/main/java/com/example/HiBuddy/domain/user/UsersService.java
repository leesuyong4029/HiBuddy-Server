package com.example.HiBuddy.domain.user;

import com.example.HiBuddy.domain.user.dto.request.UsersRequestDto;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.exception.handler.UsersHandler;
import com.example.HiBuddy.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    @Transactional
    public void deleteUser(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
        usersRepository.delete(user);
    }

    @Transactional
    public UsersRequestDto.UserNicknameDto updateUserNickname(UserDetails userDetails, UsersRequestDto.UserNicknameDto userNicknameDto) {
        String username = userDetails.getUsername();
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
        user.setNickname(userNicknameDto.getNickname());
        usersRepository.save(user);
        return userNicknameDto;
    }

    @Transactional
    public UsersRequestDto.UserOnboardingDto onboardingData(UserDetails user, UsersRequestDto.UserOnboardingDto dto) {
        String username = user.getUsername();
        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));

        users.setCountry(dto.getCountry());
        users.setMajor(dto.getMajor());
        users.setNickname(dto.getNickname());
        usersRepository.save(users);

        return dto;
    }

    @Transactional
    public Long getUserId(UserDetails user) {
        String username = user.getUsername();
        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));
        return users.getId();
    }
}


