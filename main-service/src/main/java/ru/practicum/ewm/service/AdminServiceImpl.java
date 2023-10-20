package ru.practicum.ewm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        userRepository.deleteById(userId);
        log.info("Deleted user with id: {}", userId);
    }
}
