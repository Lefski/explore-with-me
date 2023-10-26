package ru.practicum.ewm.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.emw.client.StatsClient;
import ru.practicum.ewm.dto.ViewStatsDto;
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
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.mapper.event.EventFullMapper;
import ru.practicum.ewm.mapper.event.EventShortMapper;
import ru.practicum.ewm.mapper.event.NewEventMapper;
import ru.practicum.ewm.mapper.participationRequest.ParticipationRequestMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.comment.Comment;
import ru.practicum.ewm.model.comment.CommentStatus;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventStatus;
import ru.practicum.ewm.model.participationRequest.ParticipationRequest;
import ru.practicum.ewm.model.participationRequest.ParticipationRequestStatus;
import ru.practicum.ewm.repository.*;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class PrivateServiceImpl implements PrivateService {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final CommentRepository commentRepository;
    private final StatsClient statsClient;


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
        } else if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new ConflictException("Event is not published");
        } else if (event.getParticipantsLimit() != 0 && getConfirmedRequests(eventId) >= event.getParticipantsLimit()) {
            throw new ConflictException("Request limit for event");
        }
        ParticipationRequestStatus status = ParticipationRequestStatus.PENDING;
        if (!event.getRequestModeration() || event.getParticipantsLimit() == 0) {
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
        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw new NotFoundException("User should be author of request to cancel it");
        }
        request.setStatus(ParticipationRequestStatus.CANCELED);
        try {
            ParticipationRequestDto savedRequest = ParticipationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(request));
            log.info("request status changed, requestDto:{}", savedRequest);
            return savedRequest;
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, Pageable page) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("user not found");
        }
        Page<Event> eventPage = eventRepository.findAllByInitiator_Id(userId, page);
        List<Event> eventList = eventPage.toList();
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        for (Event event :
                eventList) {
            eventShortDtos.add(EventShortMapper.toEventShortDto(event, getConfirmedRequests(event.getId()), getEventViews(event.getId())));
        }
        log.info("get events by user request completed, eventShortDtos:{}", eventShortDtos);
        return eventShortDtos;
    }

    @Override
    public EventFullDto getEventByInitiator(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new NotFoundException("event is not available for this user");
        }
        EventFullDto eventFullDto = EventFullMapper.toEventFullDto(event, getConfirmedRequests(eventId), getEventViews(eventId));
        log.info("get full event info by initiator request completed, eventFullDto:{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsByInitiator(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new NotFoundException("event is not available for this user");
        }
        List<ParticipationRequest> requestList = participationRequestRepository.findAllByEvent_Id(eventId);
        List<ParticipationRequestDto> requestDtos = new ArrayList<>();
        for (ParticipationRequest request :
                requestList) {
            requestDtos.add(ParticipationRequestMapper.toParticipationRequestDto(request));
        }
        log.info("get all requests by event initiator request completed, requests:{}", requestDtos);
        return requestDtos;

    }

    @Override
    public EventRequestStatusUpdateResult patchParticipationRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new NotFoundException("event is not available for this user");
        }
        if (!event.getRequestModeration() || event.getParticipantsLimit() == 0) {
            return confirmAllRequests(updateRequest);
        }

        if (updateRequest.getStatus().equals(ParticipationRequestStatus.CONFIRMED)) {
            Integer participantsLimit = event.getParticipantsLimit();
            Long confirmedAmount = getConfirmedRequests(eventId);
            return getConfirmedUpdateResult(updateRequest, participantsLimit, confirmedAmount);
        } else if (updateRequest.getStatus().equals(ParticipationRequestStatus.REJECTED)) {
            return rejectAllRequests(updateRequest);
        } else {
            throw new ValidationException("Change request from PENDING to PENDING status is not available");
        }


    }

    private EventRequestStatusUpdateResult confirmAllRequests(EventRequestStatusUpdateRequest updateRequest) {
        List<ParticipationRequest> participationRequests = participationRequestRepository.findAllByIdIn(updateRequest.getRequestIds());
        List<ParticipationRequestDto> participationRequestDtos = new ArrayList<>();
        for (ParticipationRequest request :
                participationRequests) {
            request.setStatus(ParticipationRequestStatus.CONFIRMED);
            participationRequestRepository.save(request);
            participationRequestDtos.add(ParticipationRequestMapper.toParticipationRequestDto(request));
        }
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(participationRequestDtos)
                .rejectedRequests(new ArrayList<>())
                .build();
    }

    private EventRequestStatusUpdateResult rejectAllRequests(EventRequestStatusUpdateRequest updateRequest) {
        List<ParticipationRequest> participationRequests = participationRequestRepository.findAllByIdIn(updateRequest.getRequestIds());
        List<ParticipationRequestDto> participationRequestDtos = new ArrayList<>();
        for (ParticipationRequest request :
                participationRequests) {
            if (request.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                request.setStatus(ParticipationRequestStatus.REJECTED);
            } else throw new ConflictException("Request status is not PENDING");
            participationRequestRepository.save(request);
            participationRequestDtos.add(ParticipationRequestMapper.toParticipationRequestDto(request));
        }
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(participationRequestDtos)
                .build();
    }

    private EventRequestStatusUpdateResult getConfirmedUpdateResult(EventRequestStatusUpdateRequest updateRequest, Integer participantsLimit, Long confirmedAmount) {
        if (confirmedAmount >= participantsLimit) {
            throw new ConflictException("Participant limit reached, no new requests allowed");
        }
        List<Long> requestsList = updateRequest.getRequestIds();
        List<ParticipationRequestDto> newConfirmedRequests = new ArrayList<>();
        while (confirmedAmount + newConfirmedRequests.size() < participantsLimit && !requestsList.isEmpty()) {
            ParticipationRequest participationRequest = participationRequestRepository.findById(requestsList.get(0))
                    .orElseThrow(() -> new NotFoundException("no such ParticipationRequest"));
            if (participationRequest.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                participationRequest.setStatus(ParticipationRequestStatus.CONFIRMED);
                participationRequestRepository.save(participationRequest);
                ParticipationRequestDto requestDto = ParticipationRequestMapper.toParticipationRequestDto(participationRequest);
                newConfirmedRequests.add(requestDto);
                requestsList.remove(0);
            } else {
                throw new ConflictException("Request status is not PENDING");
            }
        }
        List<ParticipationRequestDto> newRejectedRequests = new ArrayList<>();
        if (!requestsList.isEmpty()) {
            for (Long id : requestsList) {
                ParticipationRequest request = participationRequestRepository.findById(id).orElseThrow(() -> new NotFoundException("no such request"));
                if (request.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                    request.setStatus(ParticipationRequestStatus.REJECTED);
                } else throw new ConflictException("Request status is not PENDING");
                participationRequestRepository.save(request);
                newRejectedRequests.add(
                        ParticipationRequestMapper.toParticipationRequestDto(request)
                );
            }
        }
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(newConfirmedRequests)
                .rejectedRequests(newRejectedRequests)
                .build();
    }

    @Override
    public EventFullDto patchEventByInitiator(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        Event oldEvent = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event not found"));
        if (oldEvent.getState().equals(EventStatus.PUBLISHED)) {
            throw new ConflictException("event is already published");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        if (!user.getId().equals(oldEvent.getInitiator().getId())) {
            throw new NotFoundException("event is not available for this user");
        }
        if (updateRequest.getEventDate() != null) {
            if (LocalDateTime.parse(updateRequest.getEventDate(), DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)).isBefore(LocalDateTime.now())) {
                throw new ru.practicum.ewm.exception.ValidationException("event date may not be in the past");
            }
        }
        Event updatedEvent = updateEvent(oldEvent, updateRequest);

        try {
            EventFullDto updatedEventDto = EventFullMapper.toEventFullDto(eventRepository.save(updatedEvent), getConfirmedRequests(eventId), getEventViews(eventId));
            log.info("patch event by initiator request completed, updatedEventDto:{}", updatedEventDto);
            return updatedEventDto;
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public Event updateEvent(Event oldEvent, UpdateEventUserRequest updateRequest) {
        oldEvent.setAnnotation(updateRequest.getAnnotation() != null ? updateRequest.getAnnotation() : oldEvent.getAnnotation());

        if (updateRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateRequest.getCategory()).orElseThrow(() -> new NotFoundException("no such category"));
            oldEvent.setCategory(category);
        }

        oldEvent.setDescription(updateRequest.getDescription() != null ? updateRequest.getDescription() : oldEvent.getDescription());
        oldEvent.setEventDate(updateRequest.getEventDate() != null ? LocalDateTime.parse(updateRequest.getEventDate(), DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)) : oldEvent.getEventDate());
        oldEvent.setLat(updateRequest.getLocation() != null ? updateRequest.getLocation().getLat() : oldEvent.getLat());
        oldEvent.setLon(updateRequest.getLocation() != null ? updateRequest.getLocation().getLon() : oldEvent.getLon());
        oldEvent.setPaid(updateRequest.getPaid() != null ? updateRequest.getPaid() : oldEvent.getPaid());
        oldEvent.setParticipantsLimit(updateRequest.getParticipantLimit() != null ? updateRequest.getParticipantLimit() : oldEvent.getParticipantsLimit());
        oldEvent.setRequestModeration(updateRequest.getRequestModeration() != null ? updateRequest.getRequestModeration() : oldEvent.getRequestModeration());

        if (updateRequest.getStateAction() != null) {
            switch (updateRequest.getStateAction()) {
                case CANCEL_REVIEW:
                    oldEvent.setState(EventStatus.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    oldEvent.setState(EventStatus.PENDING);
                    break;
            }
        }

        oldEvent.setTitle(updateRequest.getTitle() != null ? updateRequest.getTitle() : oldEvent.getTitle());

        return oldEvent;
    }

    public Long getEventViews(Long eventId) {

        String uri = "/events/" + eventId.toString();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", LocalDateTime.now().minusYears(10).format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
        parameters.put("end", LocalDateTime.now().plusYears(10).format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
        parameters.put("uris", uri);
        parameters.put("unique", true);
        List<ViewStatsDto> stats = statsClient.getViewStats(parameters);
        if (stats.isEmpty()) {
            return 0L;
        } else
            return stats.get(0).getHits();
        //todo make mass request to stats server
    }

    public Long getConfirmedRequests(Long eventId) {
        return participationRequestRepository.countByEvent_IdAndStatus(eventId, ParticipationRequestStatus.CONFIRMED);
    }

    @Override
    public CommentDto postComment(NewCommentDto newCommentDto, Long userId) {
        User creator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("no such user"));
        Event event = eventRepository.findById(newCommentDto.getEventId()).orElseThrow(() -> new NotFoundException("no such event"));
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new ValidationException("Event is not published");
        }
        Comment comment = CommentMapper.toComment(newCommentDto, creator);
        try {
            CommentDto savedComment = CommentMapper.toCommentDto(commentRepository.save(comment));
            log.info("Post comment request completed, saved comment:{}", savedComment);
            return savedComment;
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public List<CommentDto> getUserComments(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("no such user");
        }
        List<Comment> commentsList = commentRepository.findAllByCreator_IdAndStatusIsNot(userId);
        List<CommentDto>
        for (Comment comment :
                commentsList) {

        }
    }

    @Override
    public CommentDto deleteCommentByUser(Long userId, Long commentId) {
        //комментарий не удаляется совсем, обновляется текст на "Комментарий удален пользователем Х + время удаления", при этом он сохраняется в БД
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("no such user");
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("no such comment"));
        if (comment.getStatus().equals(CommentStatus.DELETED)) {
            throw new ConflictException("comment is already deleted");
        }
        comment.setStatus(CommentStatus.DELETED);
        comment.setUpdated(LocalDateTime.now());
        comment.setText("Пользователь c id=" + userId + " удалил свой комментарий");
        try {
            CommentDto deletedComment = CommentMapper.toCommentDto(commentRepository.save(comment));
            log.info("Delete comment request completed, comment:{}", deletedComment);
            return deletedComment;
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public CommentDto patchCommentByUser(Long userId, Long commentId, UpdateCommentDto updateCommentDto) {
        return null;
    }
}
