package com.example.configservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConfigServiceApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldDefaultConfigurationAvailable() {
        var entity = restTemplate
                .getForEntity("/application/default", Environment.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldDefaultConfigurationAvailableUsingWebTestClient() {
        webTestClient.get()
                .uri("/application/default")
                .exchange()
                .expectStatus()
                .isOk();
    }
}
