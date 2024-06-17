package com.example.HiBuddy.domain.script;

import com.example.HiBuddy.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping
@RestController("/v1/tests")
public class ScriptsController {

    private final ScriptsService;

    @GetMapping("/scripts/{difficulty}")
    public ApiResponse<Scripts> selectScript() {

    }

}
