package ru.practicum.ewm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.event.EventFullMapper;
import ru.practicum.ewm.mapper.event.NewEventMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class PrivateServiceImpl implements PrivateService {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public PrivateServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public EventFullDto postEvent(NewEventDto newEventDto, Long userId) {
        try {
            if (LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)).isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException("EventDate is too early");
            }
            User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
            Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new NotFoundException("Category not found"));
            Event event = NewEventMapper.toEvent(newEventDto, category, initiator);
            Event savedEvent = eventRepository.save(event);
            log.info("post request completed for event:{}", event);
            Long eventId = savedEvent.getId();
            return EventFullMapper.toEventFullDto(event, getConfirmedRequests(eventId), getEventViews(eventId));
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }

    }

    private Long getEventViews(Long eventId) {
        return 0L;
    }

    private Long getConfirmedRequests(Long eventId) {
        return 0L;
    }
}
