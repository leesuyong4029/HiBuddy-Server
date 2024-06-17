package com.example.HiBuddy.domain.test;

import com.example.HiBuddy.domain.script.ScriptsRepository;
import com.example.HiBuddy.domain.script.Scripts;
import com.example.HiBuddy.domain.user.Users;
import com.example.HiBuddy.domain.user.UsersRepository;
import com.example.HiBuddy.global.config.ClovaSpeechService;
import com.example.HiBuddy.global.config.PraatService;
import com.example.HiBuddy.global.response.code.resultCode.ErrorStatus;
import com.example.HiBuddy.global.response.exception.handler.ScriptsHandler;
import com.example.HiBuddy.global.response.exception.handler.UsersHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class TestsService {

    private final UsersRepository usersRepository;
    private final ScriptsRepository scriptsRepository;
    private final TestsRepository testsRepository;
    private final ClovaSpeechService clovaSpeechService;
    private final PraatService praatService;
    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance();

    public Test performTest(Long userId, Long scriptId, MultipartFile audioFile, String testDateStr) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersHandler(ErrorStatus.USER_NOT_FOUND));
        Scripts scripts = scriptsRepository.findById(scriptId)
                .orElseThrow(() -> new ScriptsHandler(ErrorStatus.SCRIPT_NOT_FOUND));

        // 음성 파일을 Clova API로 인식
        String recognizedText = clovaSpeechService.recognizeSpeech(convertMultipartFileToBytes(audioFile));

        // 발음 점수 계산
        int pronunciationScore = calculatePronunciationScore(scripts.getText(), recognizedText);

        // 음성 파일 저장
        String audioFilePath = saveAudioFile(audioFile);

        // 피치 분석
        double meanPitch = praatService.analyzePitch(audioFilePath);
        double basePitch = 110.0;  // 기준 피치
        String pitchLevel = determinePitchLevel(meanPitch, basePitch);

        // 테스트 날짜 변환
        LocalDateTime testDate = LocalDateTime.parse(testDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        // Test 엔티티 생성 및 저장
        Test test = new Test();
        test.setUser(user);
        test.setScriptName(scripts.getScriptName());
        test.setTestDate(testDate);
        test.setDifficulty(scripts.getDifficulty().name());
        test.setRecognizedText(recognizedText);
        test.setPitch(meanPitch);
        test.setBasePitch(basePitch);
        test.setPitchLevel(pitchLevel);
        test.setPronunciationScore(pronunciationScore);

        return testsRepository.save(test);
    }

    private byte[] convertMultipartFileToBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (Exception e) {
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
            String filePath = "/tmp/" + fileName;
            File file = new File(filePath);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(audioFile.getBytes());
            }
            return filePath;
        } catch (Exception e) {
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
}

