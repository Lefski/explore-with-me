package ru.practicum.emw.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.EndpointHitDto;

@Service
public class EndpointHitClient {
    private static final String API_PREFIX = "/hit";
    //private static final String SERVER_URL = "http://localhost:9090";
    private static final String SERVER_URL = "http://stats-server:9090";
    private final RestTemplate restTemplate;

    public EndpointHitClient(@Value(SERVER_URL) String serverUrl, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }


    public ResponseEntity<EndpointHitDto> makeHit(EndpointHitDto endpointHitDto) {
        return restTemplate.postForEntity("", endpointHitDto, EndpointHitDto.class);
    }
}
