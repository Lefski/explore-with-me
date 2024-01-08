package ru.practicum.ewm.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.filter.AdminSearchFilter;
import ru.practicum.ewm.dto.user.UserDto;

import java.util.List;

public interface AdminService {
    UserDto postUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> ids, Pageable page);

    void deleteUser(Long userId);

    CategoryDto postCategory(CategoryDto categoryDto);

    void deleteCategory(Long categoryId);

    CategoryDto patchCategory(Long catId, CategoryDto categoryDto);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateRequest);

    CompilationDto postCompilationDto(NewCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationDto patchCompilation(Long compId, UpdateCompilationRequest updateRequest);

    List<EventFullDto> getEvents(AdminSearchFilter adminSearchFilter, PageRequest page);

    void deleteComment(Long commentId);

    void deleteEvent(Long eventId);

}
