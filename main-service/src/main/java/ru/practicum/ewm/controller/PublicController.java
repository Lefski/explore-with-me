package ru.practicum.ewm.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.PublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping(path = "/")
public class PublicController {

    private final PublicService publicService;

    @Autowired
    public PublicController(PublicService publicService) {
        this.publicService = publicService;
    }

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


}
