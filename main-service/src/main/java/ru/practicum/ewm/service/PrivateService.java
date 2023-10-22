package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.participationRequest.ParticipationRequestDto;

import java.util.List;

public interface PrivateService {

    EventFullDto postEvent(NewEventDto newEventDto, Long userId);

    ParticipationRequestDto postParticipationRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getParticipationRequests(Long userId);

    ParticipationRequestDto patchParticipationRequest(Long userId, Long requestId);
}
