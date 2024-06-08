package com.example.HiBuddy.domain.info;

import com.example.HiBuddy.domain.user.Country;
import com.example.HiBuddy.domain.user.Major;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/v1/info")
public class OnboardingController {
    @GetMapping("/majors")
    public ResponseEntity<List<String>> getAllMajors() {
        List<String> majors = Arrays.stream(Major.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(majors);
    }

    @GetMapping("/countries")
    public ResponseEntity<List<String>> getAllCountries() {
        List<String> countries = Arrays.stream(Country.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(countries);
    }
}
