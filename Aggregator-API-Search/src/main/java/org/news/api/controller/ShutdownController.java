package org.news.api.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShutdownController {

    private final ApplicationContext appContext;

    public ShutdownController(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @GetMapping("/shutdown")
    public void shutdownContext() {
        System.exit(SpringApplication.exit(appContext));
    }
}
