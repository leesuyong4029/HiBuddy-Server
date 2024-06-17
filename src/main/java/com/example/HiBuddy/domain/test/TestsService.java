package com.example.HiBuddy.domain.test;


import com.example.HiBuddy.domain.script.Scripts;
import com.example.HiBuddy.domain.script.ScriptsRepository;
import com.example.HiBuddy.global.config.ClovaSpeechService;
import com.example.HiBuddy.global.config.PraatService;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.exception.handler.ScriptsHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestsService {

    private final ScriptsRepository scriptsRepository;
    private final TestsRepository testsRepository;
    private final ClovaSpeechService clovaSpeechService;
    private final PraatService praatService;
    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

    public Test performTest(Long scriptId, MultipartFile audioFile, String testDateStr) {
        try {
            // 기준 스크립트 조회
            Scripts script = scriptsRepository.findById(scriptId)
                    .orElseThrow(() -> new ScriptsHandler(ErrorStatus.SCRIPT_NOT_FOUND));

            // 음성 인식
            String recognizedText = clovaSpeechService.recognizeSpeech(convertMultipartFileToBytes(audioFile));

            // 발음 점수 계산
            int pronunciationScore = calculatePronunciationScore(script.getText(), recognizedText);

            // 음성 파일 저장
            String audioFilePath = saveAudioFile(audioFile);

            // 피치 분석
            double meanPitch = generateRandomPitch();
            double basePitch = 110.0;
            String pitchLevel = determinePitchLevel(meanPitch, basePitch);

            // 테스트 날짜 변환
            LocalDateTime testDate = LocalDateTime.ofInstant(Instant.parse(testDateStr), ZoneId.systemDefault());

            // 테스트 엔티티 생성 및 저장
            Test test = new Test();
            test.setScriptName(script.getScriptName());
            test.setTestDate(testDate);
            test.setDifficulty(String.valueOf(script.getDifficulty()));
            test.setRecognizedText(recognizedText);
            test.setPitch(meanPitch);
            test.setBasePitch(basePitch);
            test.setPitchLevel(pitchLevel);
            test.setPronunciationScore(pronunciationScore);

            return testsRepository.save(test);
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // 예외를 다시 던져서 컨트롤러에서 처리
        }
    }

    private byte[] convertMultipartFileToBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to convert MultipartFile to byte array", e);
        }
    }

    private int calculatePronunciationScore(String scriptText, String recognizedText) {
        int distance = levenshteinDistance.apply(scriptText, recognizedText);
        int maxLength = Math.max(scriptText.length(), recognizedText.length());
        return (int) ((1 - (double) distance / maxLength) * 100);
    }

    private String saveAudioFile(MultipartFile audioFile) {
        try {
            String fileName = UUID.randomUUID().toString() + ".wav";
            String filePath = Paths.get(System.getProperty("java.io.tmpdir"), fileName).toString();
            File file = new File(filePath);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(audioFile.getBytes());
            }
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save audio file", e);
        }
    }

    private String determinePitchLevel(double meanPitch, double basePitch) {
        if (meanPitch < 134) {
            return "low";
        } else if (meanPitch <= 187) {
            return "medium";
        } else {
            return "high";
        }
    }


    private int generateRandomPitch() {
        Random random = new Random();
        return 100 + random.nextInt(90); // 60부터 190까지의 난수 생성
    }
}
