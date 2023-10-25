package ru.practicum.ewm.mapper.event;

import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class NewEventMapper {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static Event toEvent(NewEventDto newEventDto, Category category, User initiator) {
        return Event.builder()
                .state(EventStatus.PENDING)
                //При преобразовании из NewEventDto всегда статус ожидания, т.к. другого быть не может
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                //Формируем координаты event из getLocation, т.к. локация приходит объектом, но хранится как 2 поля
                .annotation(newEventDto.getAnnotation())
                .category(category)
                //Категорию передаем в маппер, т.к. она хранится как объект, но передается только первичный ключ категории
                .initiator(initiator)
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .paid(newEventDto.getPaid())
                .participantsLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

}
