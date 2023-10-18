package ru.yandex.practicum.ewm.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ewm.dto.EndpointHitDto;
import ru.yandex.practicum.ewm.dto.ViewStatsDto;
import ru.yandex.practicum.ewm.mapper.EndpointHitMapper;
import ru.yandex.practicum.ewm.model.EndpointHit;
import ru.yandex.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    StatsRepository repository;

    @Override
    public EndpointHitDto createEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        EndpointHitDto savedEndpointHit = EndpointHitMapper.toDto(repository.save(endpointHit));
        log.info("сохранен EndPointHit: {}", savedEndpointHit);
        return savedEndpointHit;
    }

    @Override
    public List<ViewStatsDto> findEndpointHitStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        List<ViewStatsDto> viewStatsDtos = new ArrayList<>();

        if (uris.isEmpty() && !unique) {
            viewStatsDtos = repository.findAllEndpointHits(start, end);
        } else if (uris.isEmpty() && unique) {
            viewStatsDtos = repository.findAllEndPointHitsWithUniqueIp(start, end);
        } else if (!uris.isEmpty() && !unique) {
            viewStatsDtos = repository.findAllEndpointHitsForSelectedUris(start, end, uris);
        } else if (!uris.isEmpty() && unique) {
            viewStatsDtos = repository.findAllEndPointHitsWithUniqueIpForSelectedUris(start, end, uris);
        }
        log.info("Statistics formed from db: {}", viewStatsDtos);
        return viewStatsDtos;
    }

}
