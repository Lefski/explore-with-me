package ru.practicum.ewm.dto.event;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.user.UserShortDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventShortDto {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private String eventDate;

    private Long id;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Long views;

    @Override
    public String toString() {
        return "EventShortDto{" +
                ", annotation='" + annotation + '\'' +
                ", categoryDto=" + category +
                ", confirmedRequests=" + confirmedRequests +
                ", eventDate='" + eventDate + '\'' +
                ", id=" + id +
                ", initiator=" + initiator +
                ", paid=" + paid +
                ", title='" + title + '\'' +
                ", views=" + views +
                '}';
    }
}
