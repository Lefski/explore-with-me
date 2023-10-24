package ru.practicum.ewm.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.mapper.compilation.CompilationDtoMapper;
import ru.practicum.ewm.mapper.event.EventShortMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.CompilationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PublicServiceImpl implements PublicService {

    private final CategoryRepository categoryRepository;
    private final CompilationRepository compilationRepository;
    private final PrivateServiceImpl privateService;

    @Autowired
    public PublicServiceImpl(CategoryRepository categoryRepository, CompilationRepository compilationRepository, PrivateServiceImpl privateService) {
        this.categoryRepository = categoryRepository;
        this.compilationRepository = compilationRepository;
        this.privateService = privateService;
    }

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
}
