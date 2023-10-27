package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.NewCommentDto;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.comment.Comment;
import ru.practicum.ewm.model.comment.CommentStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentMapper {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


    public static Comment toComment(NewCommentDto newCommentDto, User creator) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .eventId(newCommentDto.getEventId())
                .creator(creator)
                .status(CommentStatus.PUBLISHED)
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        UserDto creatorDto = UserMapper.toUserDto(comment.getCreator());
        LocalDateTime updateTime;
        if (comment.getStatus().equals(CommentStatus.PUBLISHED)) {
            updateTime = comment.getCreated();
        } else updateTime = comment.getUpdated();
        //updateTime = последнее время изменения комментария
        return CommentDto.builder()
                .id(comment.getId())
                .eventId(comment.getEventId())
                .creator(creatorDto)
                .text(comment.getText())
                .updateTime(updateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .build();
    }


}
