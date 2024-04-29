package com.example.HiBuddy.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hibuddy/v1/users")
public class UsersController {

    private final UsersService usersService;
    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PatchMapping("/{id}/nickname")
    public ResponseEntity<Users> updateNickname(@PathVariable Long id, @RequestBody UpdateNicknameRequest request) {
        Users updatedUsers = usersService.updateNickname(id, request.getNickname());
        return ResponseEntity.ok(updatedUsers);
    }
    private static class UpdateNicknameRequest {
        private String nickname;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
