package ru.practicum.ewm.dto.event;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.event.EventStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private String annotation;

    private Category category;

    private Long confirmedRequests;

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private String createdOn;

    private String description;

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private String eventDate;

    private Long id;

    private User initiator;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private EventStatus state;

    private String title;

    private Long views;

}
