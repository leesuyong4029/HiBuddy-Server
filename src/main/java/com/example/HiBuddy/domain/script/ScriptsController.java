package com.example.HiBuddy.domain.script;

import com.example.HiBuddy.domain.script.dto.response.ScriptsResponseDto;
import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.code.resultCode.SuccessStatus;
import com.example.HiBuddy.global.response.exception.handler.ScriptsHandler;
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


    // 새로운 메서드 추가
    @GetMapping("/scripts/{scriptId}")
    public ApiResponse<ScriptsResponseDto.ScriptDto> getScriptById(@PathVariable Long scriptId) {
        ScriptsResponseDto.ScriptDto scriptDto = scriptsService.findScriptById(scriptId)
                .orElseThrow(() -> new ScriptsHandler(ErrorStatus.SCRIPT_NOT_FOUND));
        return ApiResponse.onSuccess(scriptDto);
    }
}
