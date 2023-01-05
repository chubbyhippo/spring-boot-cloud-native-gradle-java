package com.example.catalogservice.web;

import com.example.catalogservice.config.ExampleProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;

@WebMvcTest(HomeController.class)
class HomeControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private ExampleProperties exampleProperties;

    @Test
    void shouldReturnGreetings() {
        when(exampleProperties.getGreeting())
                .thenReturn("Hello");

        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo("Hello");
    }


}