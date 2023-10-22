package ru.practicum.ewm.mapper.participationRequest;

import ru.practicum.ewm.dto.participationRequest.ParticipationRequestDto;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.participationRequest.ParticipationRequest;

import java.time.format.DateTimeFormatter;

public class ParticipationRequestMapper {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static ParticipationRequest toParticipationRequest(ParticipationRequestDto dto, User requester, Event event) {
        return ParticipationRequest.builder()
                .status(dto.getStatus())
                .requester(requester)
                .event(event)
                .build();
    }

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .status(participationRequest.getStatus())
                .requester(participationRequest.getRequester().getId())
                .event(participationRequest.getEvent().getId())
                .created(participationRequest.getCreated().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .build();
    }

}
