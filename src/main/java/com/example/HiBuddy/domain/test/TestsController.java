package com.example.HiBuddy.domain.test;

import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/perform")
    public ResponseEntity<ApiResponse<?>> performTest(
            @RequestParam Long scriptId,
            @RequestParam MultipartFile audioFile,
            @RequestParam String testDateStr) {
        try {
            Test result = testsService.performTest(scriptId, audioFile, testDateStr);
            return ResponseEntity.ok(ApiResponse.onSuccess(result));
        } catch (Exception e) {
            e.printStackTrace(); // 로그에 예외 정보 출력
            return ResponseEntity.status(ErrorStatus.INTERNAL_SERVER_ERROR.getHttpStatus())
                    .body(ApiResponse.onFailure(
                            ErrorStatus.INTERNAL_SERVER_ERROR.getCode(),
                            e.getMessage(), // 예외 메시지 포함
                            null));
        }
    }
}

