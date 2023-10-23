package ru.practicum.ewm.dto.event;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEventUserRequest {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    StateAction stateAction;
    @Size(max = 2000, min = 20, message = "annotation size may not be >2000 or <20")
    private String annotation;
    private Long category;
    @Size(max = 7000, min = 20, message = "description size may not be >7000 or <20")
    private String description;
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private String eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(max = 120, min = 3, message = "title size may not be <3 or  >120")
    private String title;

    @Override
    public String toString() {
        return "UpdateEventUserRequest{" +
                "stateAction=" + stateAction +
                ", annotation='" + annotation + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", requestModeration=" + requestModeration +
                ", title='" + title + '\'' +
                '}';
    }
}
