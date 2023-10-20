package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.model.User;

public class UserMapper {

    public static User toUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }


}
