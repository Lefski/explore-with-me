package ru.practicum.ewm.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.filter.PublicSearchFilter;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.mapper.compilation.CompilationDtoMapper;
import ru.practicum.ewm.mapper.event.EventFullMapper;
import ru.practicum.ewm.mapper.event.EventShortMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.comment.Comment;
import ru.practicum.ewm.model.comment.CommentComparator;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventStatus;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.dto.EventSpecification.*;

@Service
@Slf4j
@AllArgsConstructor
public class PublicServiceImpl implements PublicService {

    private final CategoryRepository categoryRepository;
    private final CompilationRepository compilationRepository;
    private final PrivateServiceImpl privateService;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;


    @Override
    public List<CategoryDto> getCategories(Pageable page) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        Page<Category> categoryPage = categoryRepository.findAll(page);
        for (Category category :
                categoryPage.getContent()) {
            categoryDtoList.add(CategoryMapper.toCategoryDto(category));
        }
        log.info("Search request for categories completed: {}", categoryDtoList);
        return categoryDtoList;
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category not found")
        ));
        log.info("Search request for categoryId={} completed, found category: {}", catId, categoryDto);
        return categoryDto;
    }

    @Override
    public List<CompilationDto> getCompilations(Pageable page) {
        List<CompilationDto> compilationDtos = new ArrayList<>();
        Page<Compilation> compilationPage = compilationRepository.findAll(page);
        for (Compilation compilation :
                compilationPage.getContent()) {
            List<Event> events = compilation.getEvents();
            List<EventShortDto> eventShortDtos = new ArrayList<>();
            for (Event event :
                    events) {
                eventShortDtos.add(EventShortMapper.toEventShortDto(event, privateService.getConfirmedRequests(event.getId()), privateService.getEventViews(event.getId())));
            }
            compilationDtos.add(CompilationDtoMapper.toCompilationDto(compilation, eventShortDtos));
        }
        log.info("Search request for compilation completed: {}", compilationDtos);
        return compilationDtos;
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("no such compilation"));
        List<Event> events = compilation.getEvents();
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        for (Event event :
                events) {
            eventShortDtos.add(EventShortMapper.toEventShortDto(event, privateService.getConfirmedRequests(event.getId()), privateService.getEventViews(event.getId())));
        }
        CompilationDto compilationDto = CompilationDtoMapper.toCompilationDto(compilation, eventShortDtos);
        log.info("Search request for compilationId={} completed, found compilation: {}", compId, compilationDto);
        return compilationDto;
    }

    @Override
    public EventFullDto getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("no such event"));
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new NotFoundException("no such published event");
        }
        EventFullDto eventFullDto = EventFullMapper.toEventFullDto(event, privateService.getConfirmedRequests(eventId), privateService.getEventViews(eventId));
        log.info("get event request completed, found event:{}", eventFullDto);
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> getEvents(PublicSearchFilter publicSearchFilter, Pageable page) {
        List<Specification<Event>> specifications = publicSearchFilterToSpecification(publicSearchFilter);
        Page<Event> eventsPage = eventRepository.findAll((checkByAnnotation(publicSearchFilter.getText()))
                .or(checkByDescription(publicSearchFilter.getText()))
                .or(specifications.stream().reduce(Specification::and).get()), page);
        List<Event> eventList = eventsPage.getContent();
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        for (Event event :
                eventList) {
            eventShortDtos.add(EventShortMapper.toEventShortDto(event, privateService.getConfirmedRequests(event.getId()), privateService.getEventViews(event.getId())));
        }
        if (eventList.isEmpty()) {
            throw new ValidationException("Request is empty");
        } else {
            return eventShortDtos;
        }
    }

    @Override
    public List<CommentDto> getComments(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("No such event");
        }
        List<Comment> comments = commentRepository.findCommentsByEventId(eventId);
        List<CommentDto> commentDtos = new ArrayList<>();
        comments.sort(new CommentComparator());
        for (Comment comment :
                comments) {
            commentDtos.add(CommentMapper.toCommentDto(comment));
        }
        log.info("get comments public request completed, size={}", commentDtos.size());
        return commentDtos;
    }
}
