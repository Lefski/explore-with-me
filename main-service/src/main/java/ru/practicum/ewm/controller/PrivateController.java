package ru.practicum.ewm.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.service.PrivateService;

import javax.validation.constraints.Positive;

@RestController
@Validated
@Slf4j
@RequestMapping(path = "/users")
public class PrivateController {

    private final PrivateService privateService;

    @Autowired
    public PrivateController(PrivateService privateService) {
        this.privateService = privateService;
    }

    @PostMapping("/{userId}/events")
    ResponseEntity<EventFullDto> postEvent(
            @PathVariable @Positive Long userId,
            @RequestBody @Validated NewEventDto newEventDto
    ) {
        return new ResponseEntity<>(privateService.postEvent(newEventDto, userId), HttpStatus.CREATED);
    }
}
