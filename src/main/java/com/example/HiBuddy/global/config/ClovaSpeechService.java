package com.example.HiBuddy.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ClovaSpeechService {

    @Value("${clova.client-id}")
    private String clientId;

    @Value("{clova.client-secret}")
    private String clientSecret;

    @Value("${clova.url}")
    private String clovaUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String recognizeSpeech(byte[] audioData) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/octet-stream");
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(audioData, headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                clovaUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        Map<String, Object> responseBody = responseEntity.getBody();
        if (responseBody != null && responseBody.containsKey("text")) {
            return responseBody.get("text").toString();
        } else {
            throw new RuntimeException("Clova API 호출 실패");
        }
    }


}
