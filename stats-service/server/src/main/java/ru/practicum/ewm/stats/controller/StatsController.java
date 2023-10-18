package ru.practicum.ewm.stats.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@Slf4j
@AllArgsConstructor
public class StatsController {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private StatsService statsService;

    @PostMapping("/hit")
    ResponseEntity<EndpointHitDto> postHit(@RequestBody @Validated EndpointHitDto endpointHitDto) {
        log.info("EndpointHit accepted: {}", endpointHitDto.toString());
        return new ResponseEntity<>(statsService.createEndpointHit(endpointHitDto), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    ResponseEntity<List<ViewStatsDto>> findEndpointHitStats(
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime end,
            @RequestParam(required = false, defaultValue = "") List<String> uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique
    ) {
        log.info("Request for stats accepted. start {}, end {}, uris {}, unique {}", start, end, uris, unique);
        if (start.isEqual(end)) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        } else if (start.isAfter(end)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(statsService.findEndpointHitStats(start, end, uris, unique), HttpStatus.OK);
    }


}
