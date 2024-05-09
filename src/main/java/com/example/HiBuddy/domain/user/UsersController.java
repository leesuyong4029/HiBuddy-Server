package com.example.HiBuddy.domain.user;

import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.global.response.code.resultCode.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hibuddy/v1/users")
public class UsersController {
    private final UsersService usersService;


    @PatchMapping("/me/nickname")
    @Operation(summary = "프로필 닉네임 수정하기 API", description = "프로필 닉네임 수정하기, UserNicknameDto 사용")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER404", description = "유저가 존재하지 않습니다", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<SuccessStatus> updateUserNickname(@AuthenticationPrincipal UserDetails user, @RequestBody UserRequestDto.UserNicknameDto userNicknameDto) {
        usersService.updateUserNickname(usersService.getUserId(user), userNicknameDto);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @DeleteMapping("deleteUser/{userId}")
    @Operation(summary = "유저 삭제 API", description = "유저 삭제하기, userId 사용")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "해당 유저가 삭제되었습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER404", description = "유저가 존재하지 않습니다", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<SuccessStatus> deleteUser(@PathVariable("userId") Long userId) {
        usersService.deleteUser(userId);
        return ApiResponse.onSuccess(SuccessStatus.USER_DELETE_SUCCESS);
    }
}
