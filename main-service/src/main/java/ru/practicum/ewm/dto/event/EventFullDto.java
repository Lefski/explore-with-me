package ru.practicum.ewm.dto.event;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.event.EventStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private String annotation;

    private CategoryDto category;

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

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private String publishedOn;

    private Boolean requestModeration;

    private EventStatus state;

    private String title;

    private Long views;

    @Override
    public String toString() {
        return "EventFullDto{" +
                ", category=" + category +
                ", confirmedRequests=" + confirmedRequests +
                ", createdOn='" + createdOn + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", id=" + id +
                ", initiator=" + initiator +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", requestModeration=" + requestModeration +
                ", state=" + state +
                ", title='" + title + '\'' +
                ", views=" + views +
                '}';
    }
}
