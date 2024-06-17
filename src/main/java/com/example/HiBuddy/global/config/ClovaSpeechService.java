package com.example.HiBuddy.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ClovaSpeechService {

    @Value("${clova.client-id}")
    private String clientId;

    @Value("${clova.client-secret}")
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

        try {
            ResponseEntity<Map> responseEntity = restTemplate.exchange(
                    clovaUrl + "?lang=Kor", // 언어 파라미터 추가
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
        } catch (HttpClientErrorException e) {
            // 400 Bad Request 예외 처리
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new RuntimeException("Clova API 잘못된 요청: " + e.getResponseBodyAsString());
            }
            throw e;
        }
    }
}
