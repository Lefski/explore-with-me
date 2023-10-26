package ru.practicum.ewm.dto.comment;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.model.comment.CommentStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private Long id;

    private Long eventId;

    private UserDto creator;

    private String text;
    
    private CommentStatus status;

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private String updateTime;
}
