package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.NewEventDto;

public interface PrivateService {

    EventFullDto postEvent(NewEventDto newEventDto, Long userId);

}
