package ru.practicum.ewm.mapper.event;

import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.Location;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.event.Event;

import java.time.format.DateTimeFormatter;

public class EventFullMapper {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static EventFullDto toEventFullDto(Event event, Long confirmedRequests, Long views) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .id(event.getId())
                .initiator(event.getInitiator())
                .location(new Location(event.getLat(), event.getLon()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantsLimit())
                .publishedOn(event.getPublishedOn() != null ? event.getPublishedOn().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)) : null)
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }
}
