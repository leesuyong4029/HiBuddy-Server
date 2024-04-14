package com.example.HiBuddy.domain.oauth.kakao;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController { // 임시용
    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }
}
