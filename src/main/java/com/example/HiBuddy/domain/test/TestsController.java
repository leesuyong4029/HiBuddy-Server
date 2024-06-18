package com.example.HiBuddy.domain.test;

import com.example.HiBuddy.domain.test.dto.response.TestsResponseDto;
import com.example.HiBuddy.domain.user.UsersService;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.HiBuddy.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/tests")
@RequiredArgsConstructor
public class TestsController {

    private final TestsService testsService;
    private final UsersService usersService;

    @PostMapping("/perform")
    public ResponseEntity<ApiResponse<?>> performTest(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam Long scriptId,
            @RequestParam MultipartFile audioFile) {
        try {
            Long userId = usersService.getUserId(user);
            TestsResponseDto.TestResultDto resultDto = testsService.performTest(userId, scriptId, audioFile);
            return ResponseEntity.ok(ApiResponse.onSuccess(resultDto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(ErrorStatus.INTERNAL_SERVER_ERROR.getHttpStatus())
                    .body(ApiResponse.onFailure(
                            ErrorStatus.INTERNAL_SERVER_ERROR.getCode(),
                            e.getMessage(),
                            null));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<?>> getTestHistory(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size) {
        try {
            page = page - 1;
            TestsResponseDto.TestHistoryMainPageListDto results = testsService.getTestHistory(usersService.getUserId(user), page, size);
            return ResponseEntity.ok(ApiResponse.onSuccess(results));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(ErrorStatus.INTERNAL_SERVER_ERROR.getHttpStatus())
                    .body(ApiResponse.onFailure(
                            ErrorStatus.INTERNAL_SERVER_ERROR.getCode(),
                            e.getMessage(),
                            null));
        }
    }

    @GetMapping("/history/{testId}")
    public ResponseEntity<ApiResponse<?>> getTestById(@PathVariable Long testId, @AuthenticationPrincipal UserDetails user) {
        try {
            TestsResponseDto.TestHistoryDto result = testsService.getTestById(testId, usersService.getUserId(user));
            return ResponseEntity.ok(ApiResponse.onSuccess(result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(ErrorStatus.INTERNAL_SERVER_ERROR.getHttpStatus())
                    .body(ApiResponse.onFailure(
                            ErrorStatus.INTERNAL_SERVER_ERROR.getCode(),
                            e.getMessage(),
                            null));
        }
    }
}


