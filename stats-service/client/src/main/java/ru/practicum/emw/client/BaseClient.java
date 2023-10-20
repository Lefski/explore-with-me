package ru.practicum.emw.client;

import org.springframework.web.client.RestTemplate;

public class BaseClient {
    private final RestTemplate restTemplate;

    public BaseClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
