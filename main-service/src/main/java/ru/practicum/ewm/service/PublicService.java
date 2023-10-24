package ru.practicum.ewm.service;


import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;

import java.util.List;

public interface PublicService {

    List<CategoryDto> getCategories(Pageable page);

    CategoryDto getCategory(Long catId);

    List<CompilationDto> getCompilations(Pageable page);

    CompilationDto getCompilation(Long compId);
}
