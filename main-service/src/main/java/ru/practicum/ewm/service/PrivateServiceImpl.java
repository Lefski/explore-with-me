package ru.practicum.ewm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.participationRequest.ParticipationRequestDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.event.EventFullMapper;
import ru.practicum.ewm.mapper.event.NewEventMapper;
import ru.practicum.ewm.mapper.participationRequest.ParticipationRequestMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.participationRequest.ParticipationRequest;
import ru.practicum.ewm.model.participationRequest.ParticipationRequestStatus;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class PrivateServiceImpl implements PrivateService {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    @Autowired
    public PrivateServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository, UserRepository userRepository, ParticipationRequestRepository participationRequestRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.participationRequestRepository = participationRequestRepository;
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

    @Override
    public ParticipationRequestDto postParticipationRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        if (participationRequestRepository.existsByRequester_IdAndEvent_Id(userId, eventId)) {
            throw new ConflictException("Request already exists");
        } else if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("User can not request to take part in his own event");
//        } else if (!event.getState().equals(EventStatus.PUBLISHED)) {
//            throw new ConflictException("Event is not published"); todo undo comment for this section
        } else if (event.getParticipantsLimit() != 0 && getConfirmedRequests(eventId) >= event.getParticipantsLimit()) {
            //todo:check if requests need to be confirmed
            throw new ConflictException("Request limit for event");
        }
        ParticipationRequestStatus status = ParticipationRequestStatus.PENDING;
        if (!event.getRequestModeration()) {
            status = ParticipationRequestStatus.CONFIRMED;
        }
        ParticipationRequest request = ParticipationRequest.builder().status(status).requester(user).event(event).build();
        try {
            ParticipationRequestDto savedRequest = (ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(request)));
            log.info("saved request:{}", savedRequest);
            return savedRequest;
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }

    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(Long userId) {
        List<ParticipationRequest> requestList = participationRequestRepository.findAllByRequester_Id(userId);
        List<ParticipationRequestDto> requestDtos = new ArrayList<>();
        for (ParticipationRequest request :
                requestList) {
            requestDtos.add(ParticipationRequestMapper.toParticipationRequestDto(request));
        }
        return requestDtos;
    }

    @Override
    public ParticipationRequestDto patchParticipationRequest(Long userId, Long requestId) {
        ParticipationRequest request = participationRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("request not found"));
        if (!Objects.equals(request.getRequester().getId(), requestId)) {
            throw new NotFoundException("User should be author of request to cancel it");
        }
        request.setStatus(ParticipationRequestStatus.REJECTED);
        ParticipationRequestDto savedRequest = ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(request));
        log.info("request status changed, requestDto:{}", savedRequest);
        return savedRequest;
    }


    private Long getEventViews(Long eventId) {
        return 0L;
    }

    private Long getConfirmedRequests(Long eventId) {
        //todo change to confirmed requests
        return participationRequestRepository.countByEvent_Id(eventId);
    }
}
