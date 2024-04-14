package com.example.HiBuddy.global.response.exception;

import com.example.HiBuddy.global.response.code.BaseErrorCode;
import com.example.HiBuddy.global.response.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDto getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
