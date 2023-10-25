package ru.practicum.ewm.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.filter.PublicSearchFilter;
import ru.practicum.ewm.service.PublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping(path = "")
@AllArgsConstructor
public class PublicController {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final PublicService publicService;
    //private final EndpointHitClient endpointHitClient;


    @GetMapping("/compilations")
    ResponseEntity<List<CompilationDto>> getCompilations(
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size
    ) {
        log.info("Get compilations request accepted from={}, size={}", from, size);
        PageRequest page = PageRequest.of(from / size, size);
        return new ResponseEntity<>(publicService.getCompilations(page), HttpStatus.OK);
    }

    @GetMapping("/compilations/{compId}")
    ResponseEntity<CompilationDto> getCompilationById(
            @PathVariable @Positive Long compId
    ) {
        log.info("get compilation request accepted, id={}", compId);
        return new ResponseEntity<>(publicService.getCompilation(compId), HttpStatus.OK);
    }

    @GetMapping("/categories")
    ResponseEntity<List<CategoryDto>> getCategories(
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size
    ) {
        log.info("Get categories request accepted from={}, size={}", from, size);
        PageRequest page = PageRequest.of(from / size, size);
        return new ResponseEntity<>(publicService.getCategories(page), HttpStatus.OK);
    }

    @GetMapping("/categories/{catId}")
    ResponseEntity<CategoryDto> getCategory(
            @PathVariable @Positive Long catId
    ) {
        log.info("get category request accepted, id={}", catId);
        return new ResponseEntity<>(publicService.getCategory(catId), HttpStatus.OK);
    }

    //todo check!!!
    @GetMapping("/events")
    ResponseEntity<List<EventShortDto>> getEvents(
            @RequestParam(required = false, defaultValue = "") String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @PositiveOrZero Integer size,
            HttpServletRequest request
    ) {
        PublicSearchFilter publicSearchFilter = PublicSearchFilter.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .build();
        PageRequest page = PageRequest.of(from / size, size);
        log.info("Get events with search filter request accepted, filter:{}", publicSearchFilter);
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("ewm-main-service")
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .build();
        //log.info("made request to stats service, endpointHitDto:{}", endpointHitClient.makeHit(endpointHitDto));
        return new ResponseEntity<>(publicService.getEvents(publicSearchFilter, page), HttpStatus.OK);
    }

    @GetMapping("/events/{id}")
    ResponseEntity<EventFullDto> getEvent(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        log.info("Get event request accepted, id={}", id);
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("ewm-main-service")
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .build();
        //log.info("made request to stats service, endpointHitDto:{}", endpointHitClient.makeHit(endpointHitDto));
        //todo реализовать нормальный доступ к статистике
        return new ResponseEntity<>(publicService.getEvent(id), HttpStatus.OK);
    }


}
