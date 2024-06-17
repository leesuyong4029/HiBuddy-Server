package com.example.HiBuddy.global.config;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class PraatService {

    public double analyzePitch(String audioFilePath) {
        try {
            // 프로젝트 상대 경로로 파이썬 스크립트 경로 설정
            String scriptPath = "src/main/resources/scripts/analyze_pitch.py";

            // 현재 작업 디렉토리 경로 가져오기
            String currentDir = System.getProperty("user.dir");

            // 전체 경로 생성
            String fullPath = Paths.get(currentDir, scriptPath).toString();

            List<String> command = new ArrayList<>();
            command.add("python3");
            command.add(fullPath);
            command.add(audioFilePath);

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); // 표준 오류를 표준 출력으로 리디렉션
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            double meanPitch = 0;


            process.waitFor();
            System.out.println("Python script output: " + output.toString()); // 디버깅 로그 출력
            return meanPitch;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to analyze pitch using Praat", e);
        }
    }
}