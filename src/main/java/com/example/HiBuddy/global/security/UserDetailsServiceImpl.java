package com.example.HiBuddy.global.security;

import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.domain.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsersRepository usersRepository;

    // 주어진 email을 가진 사용자의 세부 정보를 얻는다.
    // 이메일은 고유하다고 간주하고, 사용자 이름이 존재하지 않으면
    // 해당 메서드가 관련 에러를 발생시킨다.
    @Override
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsImpl(user);
    }
}
