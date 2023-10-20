package ru.practicum.ewm.stats.mapper;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EndpointHitMapper {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


    public static EndpointHitDto toDto(EndpointHit endpointHit) {
        return new EndpointHitDto(
                endpointHit.getId(),
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));//TODO:check how works
    }

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(
                endpointHitDto.getIp(),
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                LocalDateTime.parse(endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
    }
}
