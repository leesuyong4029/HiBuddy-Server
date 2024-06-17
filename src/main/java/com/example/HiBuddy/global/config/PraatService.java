package com.example.HiBuddy.global.config;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class PraatService {

    public double analyzePitch(String audioFilePath) {
        try {
            List<String> command = new ArrayList<>();
            command.add("python3");
            command.add("C:\\Users\\edgar\\Desktop\\HiBuddy-AI\\analyze_pitch.py");
            command.add(audioFilePath);

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            double meanPitch = 0;

            while ((line = reader.readLine()) != null) {
                meanPitch = Double.parseDouble(line.trim());
            }

            process.waitFor();
            return meanPitch;
            } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to analyze pitch using Praat", e);

        }
    }
}
