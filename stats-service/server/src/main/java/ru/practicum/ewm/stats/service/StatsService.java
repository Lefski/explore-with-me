package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;


public interface StatsService {
    EndpointHitDto createEndpointHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> findEndpointHitStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);


}
