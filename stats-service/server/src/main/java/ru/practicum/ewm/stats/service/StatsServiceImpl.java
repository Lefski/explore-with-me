package ru.practicum.ewm.stats.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.stats.mapper.EndpointHitMapper;
import ru.practicum.ewm.stats.model.EndpointHit;
import ru.practicum.ewm.stats.repository.StatsRepository;

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
        log.info("saved EndPointHit: {}", savedEndpointHit);
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
        log.info("Statistics formed from db with size: {}", viewStatsDtos.size());
        return viewStatsDtos;
    }

}
