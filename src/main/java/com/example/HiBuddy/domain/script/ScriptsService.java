package com.example.HiBuddy.domain.script;

import com.example.HiBuddy.domain.script.dto.response.ScriptsResponseDto;
import com.example.HiBuddy.global.response.ApiResponse;
import com.example.HiBuddy.global.response.code.resultCode.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScriptsService {

    private final ScriptsRepository scriptsRepository;

    public ApiResponse<SuccessStatus> saveScript(Scripts script) {
        scriptsRepository.save(script);
        return ApiResponse.onSuccess(SuccessStatus.SCRIPT_ADD_SUCCESS);
    }
    public List<ScriptsResponseDto.ScriptDto> findAllScripts() {
        List<Scripts> scripts = scriptsRepository.findAll();
        return scripts.stream()
                .map(script -> ScriptsResponseDto.ScriptDto.builder()
                        .scriptId(script.getId())
                        .scriptName(script.getScriptName())
                        .difficulty(script.getDifficulty().name())
                        .text(script.getText())
                        .build())
                .collect(Collectors.toList());
    }
}
