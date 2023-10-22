package ru.practicum.ewm.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.participationRequest.ParticipationRequestDto;
import ru.practicum.ewm.service.PrivateService;

import javax.validation.constraints.Positive;
import java.util.List;

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
        log.info("Post request accepted, newEventDto:{}, userId={}", newEventDto, userId);
        return new ResponseEntity<>(privateService.postEvent(newEventDto, userId), HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/requests")
    ResponseEntity<ParticipationRequestDto> postParticipationRequest(
            @PathVariable @Positive Long userId,
            @RequestParam @Positive Long eventId
    ) {
        log.info("Post request accepted, eventId={}, userId={}", eventId, userId);

        return new ResponseEntity<>(privateService.postParticipationRequest(userId, eventId), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/requests")
    ResponseEntity<List<ParticipationRequestDto>> getParticipationRequests(
            @PathVariable @Positive Long userId
    ) {
        log.info("Get request for ParticipationRequests accepted for userId={}", userId);
        return new ResponseEntity<>(privateService.getParticipationRequests(userId), HttpStatus.OK);
    }

    @PatchMapping("/    {userId}/requests/{requestId}/cancel")
    ResponseEntity<ParticipationRequestDto> patchParticipationRequest(
            @RequestParam @Positive Long userId,
            @PathVariable @Positive Long requestId
    ) {
        log.info("Patch request accepted for requestId={}, userId={}", requestId, userId);
        return new ResponseEntity<>(privateService.patchParticipationRequest(userId, requestId), HttpStatus.OK);
    }


}
