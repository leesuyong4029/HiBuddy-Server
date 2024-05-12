package com.example.HiBuddy.domain.user;

import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.exception.handler.UsersHandler;
import com.example.HiBuddy.global.security.UserDetailsImpl;
import com.example.HiBuddy.global.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final UserDetailsServiceImpl userDetailsService;

    public Long getUserId(UserDetails user){
        String username = user.getUsername();
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        return userDetails.getUserId();
    }
    @Transactional
    public void deleteUser(Long userId){
        Users user = usersRepository.findUsersById(userId).orElseThrow(() -> new UsernameNotFoundException("User with ID " + userId + " not found"  ));
        usersRepository.delete(user);
    }

    @Transactional
    public UsersRequestDto.UserNicknameDto updateUserNickname(Long userId, UsersRequestDto.UserNicknameDto userNicknameDto){
        Users user = usersRepository.findById(userId).orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));
        user.setNickname(userNicknameDto.getNickname());
        usersRepository.save(user);
        return userNicknameDto;
    }

}
