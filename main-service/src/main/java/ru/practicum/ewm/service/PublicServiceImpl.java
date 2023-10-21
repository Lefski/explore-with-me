package ru.practicum.ewm.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PublicServiceImpl implements PublicService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public PublicServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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
}
