package com.example.HiBuddy.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseGet(() -> {
            // 이메일이 일치하는 사용자가 없으면 새로운 User 객체를 생성
            User newUser = new User();
            return newUser;
        });
    }

    @Transactional
    public int join(User user) {
        user.setUserRole(UserRole.USER);
        try {
            userRepository.save(user);
            return 1;
        } catch (Exception e) {
            return -1;
        }

    }

    @Transactional
    public User updateNickname(Long userId, String newNickname) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        User user = userOptional.get();
        user.setNickname(newNickname);
        return userRepository.save(user);
    }
    @Transactional
    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(
                ()->new IllegalArgumentException("Unexpected user"));
    }
}
