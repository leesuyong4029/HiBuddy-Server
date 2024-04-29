package com.example.HiBuddy.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @Transactional
    public Users findByEmail(String email){
        return usersRepository.findByEmail(email).orElseGet(() -> {
            // 이메일이 일치하는 사용자가 없으면 새로운 User 객체를 생성
            Users newUsers = new Users();
            return newUsers;
        });
    }

    @Transactional
    public int join(Users users) {
        try {
            usersRepository.save(users);
            return 1;
        } catch (Exception e) {
            return -1;
        }

    }

    @Transactional
    public Users updateNickname(Long userId, String newNickname) {
        Optional<Users> userOptional = usersRepository.findById(userId);

        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        Users users = userOptional.get();
        users.setNickname(newNickname);
        return usersRepository.save(users);
    }
    @Transactional
    public Users findById(Long userId){
        return usersRepository.findById(userId).orElseThrow(
                ()->new IllegalArgumentException("Unexpected user"));
    }
}
