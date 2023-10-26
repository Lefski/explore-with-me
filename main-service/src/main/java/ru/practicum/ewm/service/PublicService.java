package ru.practicum.ewm.service;


import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.filter.PublicSearchFilter;

import java.util.List;

public interface PublicService {

    List<CategoryDto> getCategories(Pageable page);

    CategoryDto getCategory(Long catId);

    List<CompilationDto> getCompilations(Pageable page);

    CompilationDto getCompilation(Long compId);

    EventFullDto getEvent(Long eventId);

    List<EventShortDto> getEvents(PublicSearchFilter publicSearchFilter, Pageable page);

    List<CommentDto> getComments(Long eventId);
}
