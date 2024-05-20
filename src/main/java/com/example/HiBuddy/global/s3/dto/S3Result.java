package com.example.HiBuddy.global.s3.dto;

import lombok.Getter;

@Getter
public class S3Result {
    private String fileUrl;

    public S3Result(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
