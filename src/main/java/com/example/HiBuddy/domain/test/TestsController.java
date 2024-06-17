package com.example.HiBuddy.domain.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/tests")
public class TestsController {

    private final TestsService testsService;



}
