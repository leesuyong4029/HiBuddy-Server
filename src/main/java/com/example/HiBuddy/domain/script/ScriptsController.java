package com.example.HiBuddy.domain.script;

import com.example.HiBuddy.domain.script.dto.response.ScriptsResponseDto;
import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.global.response.code.resultCode.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/v1/tests")
@RestController
public class ScriptsController {

    private final ScriptsService scriptsService;

    @PostMapping("/scripts")
    public ApiResponse<SuccessStatus> addScript(@RequestBody Scripts script) {
        return scriptsService.saveScript(script);
    }
    @GetMapping("/scripts")
    public ApiResponse<Map<String, List<ScriptsResponseDto.ScriptDto>>> getAllScripts() {
        List<ScriptsResponseDto.ScriptDto> scriptDtos = scriptsService.findAllScripts();
        Map<String, List<ScriptsResponseDto.ScriptDto>> result = Map.of("script", scriptDtos);
        return ApiResponse.onSuccess(result);
    }
}
