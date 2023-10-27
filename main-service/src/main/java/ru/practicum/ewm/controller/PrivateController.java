package ru.practicum.ewm.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.NewCommentDto;
import ru.practicum.ewm.dto.comment.UpdateCommentDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.dto.participationRequest.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.participationRequest.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.participationRequest.ParticipationRequestDto;
import ru.practicum.ewm.service.PrivateService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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

    @GetMapping("/{userId}/events")
    ResponseEntity<List<EventShortDto>> getEventsByUser(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size
    ) {
        log.info("Get all events by user request accepted, for userId={}, from={}, size={}", userId, from, size);
        Pageable page = PageRequest.of(from / size, size);
        return new ResponseEntity<>(privateService.getEventsByUser(userId, page), HttpStatus.OK);
    }

    @PostMapping("/{userId}/events")
    ResponseEntity<EventFullDto> postEvent(
            @PathVariable @Positive Long userId,
            @RequestBody @Validated NewEventDto newEventDto
    ) {
        log.info("Post request accepted, newEventDto:{}, userId={}", newEventDto, userId);
        return new ResponseEntity<>(privateService.postEvent(newEventDto, userId), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/events/{eventId}")
    ResponseEntity<EventFullDto> getEventByInitiator(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        log.info("Get full event info from initiator accepted, userId={}, eventId={}", userId, eventId);
        return new ResponseEntity<>(privateService.getEventByInitiator(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    ResponseEntity<EventFullDto> patchEventByUser(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody @Validated UpdateEventUserRequest updateEventUserRequest
    ) {
        log.info("Patch update event request from initiator accepted, userId={}, eventId={}, request:{}", userId, eventId, updateEventUserRequest);
        return new ResponseEntity<>(privateService.patchEventByInitiator(userId, eventId, updateEventUserRequest), HttpStatus.OK);

    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    ResponseEntity<List<ParticipationRequestDto>> getAllParticipationRequestsByInitiator(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        log.info("Get all requests for event by initiator, userId={}, eventId={}", userId, eventId);
        return new ResponseEntity<>(privateService.getParticipationRequestsByInitiator(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    ResponseEntity<EventRequestStatusUpdateResult> patchParticipationRequestsStatusByOwner(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody @Validated EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {
        log.info("Patch requests for event by initiator, userId={}, eventId={}, updateRequest:{}", userId, eventId, eventRequestStatusUpdateRequest);
        return new ResponseEntity<>(privateService.patchParticipationRequests(userId, eventId, eventRequestStatusUpdateRequest), HttpStatus.OK);
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

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    ResponseEntity<ParticipationRequestDto> patchParticipationRequest(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long requestId
    ) {
        log.info("Patch request accepted for requestId={}, userId={}", requestId, userId);
        return new ResponseEntity<>(privateService.patchParticipationRequest(userId, requestId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/comments")
    ResponseEntity<CommentDto> postComment(
            @PathVariable @Positive Long userId,
            @RequestBody @Validated NewCommentDto newCommentDto
    ) {
        log.info("Post comment request accepted, userId={}, newCommentDto={}", userId, newCommentDto);
        return new ResponseEntity<>(privateService.postComment(newCommentDto, userId), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/comments")
    ResponseEntity<List<CommentDto>> getCommentsByCreator(
            @PathVariable @Positive Long userId
    ) {
        log.info("Get comments request accepted, userId={}", userId);
        return new ResponseEntity<>(privateService.getUserComments(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    ResponseEntity<CommentDto> deleteCommentById(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long commentId
    ) {
        log.info("Delete comment by creator request accepted, userId={}, commentId={}", userId, commentId);
        return new ResponseEntity<>(privateService.deleteCommentByUser(userId, commentId), HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    ResponseEntity<CommentDto> patchComment(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long commentId,
            @RequestBody @Validated UpdateCommentDto updateCommentDto
    ) {
        log.info("Patch comment by creator request accepted, userId={}, commentId={}, updateCommentDto:{}", userId, commentId, updateCommentDto);
        return new ResponseEntity<>(privateService.patchCommentByUser(userId, commentId, updateCommentDto), HttpStatus.OK);
    }

}
