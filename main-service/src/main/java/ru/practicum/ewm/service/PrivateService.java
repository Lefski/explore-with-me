package ru.practicum.ewm.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.dto.participationRequest.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.participationRequest.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.participationRequest.ParticipationRequestDto;

import java.util.List;

public interface PrivateService {

    EventFullDto postEvent(NewEventDto newEventDto, Long userId);

    ParticipationRequestDto postParticipationRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getParticipationRequests(Long userId);

    ParticipationRequestDto patchParticipationRequest(Long userId, Long requestId);

    List<EventShortDto> getEventsByUser(Long userId, Pageable page);

    EventFullDto getEventByInitiator(Long userId, Long eventId);

    List<ParticipationRequestDto> getParticipationRequestsByInitiator(Long userId, Long eventId);

    EventRequestStatusUpdateResult patchParticipationRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);

    EventFullDto patchEventByInitiator(Long userId, Long eventId, UpdateEventUserRequest updateRequest);
}
