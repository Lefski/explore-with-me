package ru.practicum.ewm.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.UserDto;

import java.util.List;

public interface AdminService {
    UserDto postUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> ids, Pageable page);

    void deleteUser(Long userId);
}
