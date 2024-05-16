package com.example.HiBuddy.domain.oauth.google;

import lombok.Data;

@Data
public class GoogleProfile {
    private String sub;
    private String email;
    private String picture;
    private boolean email_verified;
}
