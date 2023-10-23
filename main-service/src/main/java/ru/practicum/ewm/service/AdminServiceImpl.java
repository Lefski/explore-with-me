package ru.practicum.ewm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.mapper.event.EventFullMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventStatus;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final PrivateServiceImpl privateService;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository, EventRepository eventRepository, PrivateServiceImpl privateService) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.privateService = privateService;
    }

    @Override
    public UserDto postUser(UserDto userDto) {
        try {
            UserDto savedUser = UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
            log.info("User created: {}", savedUser);
            return savedUser;
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Pageable page) {
        List<User> userList;
        if (ids.isEmpty()) {
            Page<User> userPage = userRepository.findAll(page);
            userList = userPage.toList();
        } else {
            userList = userRepository.findAllByIdIn(ids, page);
        }
        List<UserDto> userDtos = new ArrayList<>();
        for (User user :
                userList) {
            userDtos.add(UserMapper.toUserDto(user));
        }
        log.info("Search request for users completed: {}", userDtos);
        return userDtos;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.deleteById(userId);
        log.info("Deleted user with id: {}", userId);
    }

    @Override
    public CategoryDto postCategory(CategoryDto categoryDto) {
        try {
            CategoryDto savedCategory = CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
            log.info("Category created: {}", savedCategory);
            return savedCategory;
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {
        try {
            categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
            categoryRepository.deleteById(categoryId);
            log.info("Deleted category with id: {}", categoryId);//TODO:проверить обрабатывается ли случай связанности данных
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public CategoryDto patchCategory(Long categoryId, CategoryDto categoryDto) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        Category category = CategoryMapper.toCategory(categoryDto);
        category.setId(categoryId);
        try {
            CategoryDto savedCategory = CategoryMapper.toCategoryDto(categoryRepository.save(category));
            log.info("Category patched: {}", savedCategory);
            return savedCategory;
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateRequest) {
        Event oldEvent = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event not found"));
        Event updatedEvent = updateEvent(oldEvent, updateRequest);
        try {
            EventFullDto updatedEventDto = EventFullMapper.toEventFullDto(eventRepository.save(updatedEvent), privateService.getConfirmedRequests(eventId), privateService.getEventViews(eventId));

            log.info("patch event by admin request completed, updatedEventDto:{}", updatedEventDto);
            return updatedEventDto;
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public Event updateEvent(Event oldEvent, UpdateEventAdminRequest updateRequest) {
        oldEvent.setAnnotation(updateRequest.getAnnotation() != null ? updateRequest.getAnnotation() : oldEvent.getAnnotation());
        if (updateRequest.getDescription() != null) {
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
                    if (!oldEvent.getState().equals(EventStatus.PUBLISHED)) {
                        //событие можно отклонить, только если оно еще не опубликовано
                        oldEvent.setState(EventStatus.CANCELED);
                    } else throw new ConflictException("Event status is not PUBLISHED, event may not be canceled");
                    break;
                case PUBLISH_EVENT:
                    if (LocalDateTime.now().isBefore(oldEvent.getEventDate().minusHours(1))) {
                        //дата начала изменяемого события должна быть не ранее чем за час от даты публикации.
                        oldEvent.setPublishedOn(LocalDateTime.now());
                    } else {
                        throw new ConflictException("Event date is too close to publication date, less than 1 hour, event date=" + oldEvent.getEventDate().toString() + " publication date =" + LocalDateTime.now());
                    }
                    if (!oldEvent.getState().equals(EventStatus.PENDING)) {
                        //событие можно публиковать, только если оно в состоянии ожидания публикации
                        throw new ConflictException("Event status is not PENDING, event may not be published");
                    }
                    oldEvent.setState(EventStatus.PUBLISHED);
                    break;
            }
        }

        oldEvent.setTitle(updateRequest.getTitle() != null ? updateRequest.getTitle() : oldEvent.getTitle());

        return oldEvent;
    }
}
