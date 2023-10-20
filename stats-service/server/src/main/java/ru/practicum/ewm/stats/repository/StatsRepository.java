package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.ewm.dto.ViewStatsDto(h.app, h.uri, count(h.ip)) " +
            "from EndpointHit h " +
            "where h.timestamp > ?1 and h.timestamp < ?2 " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<ViewStatsDto> findAllEndpointHits(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ewm.dto.ViewStatsDto(h.app, h.uri, count(h.ip)) " +
            "from EndpointHit h " +
            "where h.timestamp > ?1 and h.timestamp < ?2 and h.uri in ?3 " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<ViewStatsDto> findAllEndpointHitsForSelectedUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.ewm.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from EndpointHit h " +
            "where h.timestamp > ?1 and h.timestamp < ?2 " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<ViewStatsDto> findAllEndPointHitsWithUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ewm.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from EndpointHit h " +
            "where h.timestamp > ?1 and h.timestamp < ?2 and h.uri in ?3 " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<ViewStatsDto> findAllEndPointHitsWithUniqueIpForSelectedUris(LocalDateTime start, LocalDateTime end, List<String> uris);

}
