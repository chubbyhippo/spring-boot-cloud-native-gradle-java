package com.example.catalogservice.web;

import com.example.catalogservice.config.ExampleProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    private final ExampleProperties exampleProperties;

    public HomeController(ExampleProperties exampleProperties) {
        this.exampleProperties = exampleProperties;
    }

    @GetMapping("/")
    public String getGreeting() {
        return exampleProperties.getGreeting();
    }


}
