package ru.practicum.ewm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
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
}
