package ru.practicum.emw.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.util.List;
import java.util.Map;

@Service
public class StatsClient {
    private static final String API_PREFIX = "/stats";
    //private final static String SERVER_URL = "http://localhost:9090";
    private final static String SERVER_URL = "http://stats-server:9090";
    private final RestTemplate restTemplate;


    public StatsClient(@Value(SERVER_URL) String serverUrl, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public List<ViewStatsDto> getViewStats(Map<String, Object> parameters) {
        try {
            ResponseEntity<List<ViewStatsDto>> response = restTemplate.exchange(
                    "?start={start}&end={end}&uris={uris}&unique={unique}",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }, parameters);
            return response.getBody();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
