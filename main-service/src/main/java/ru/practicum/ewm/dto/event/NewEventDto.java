package ru.practicum.ewm.dto.event;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private Long id;

    @NotBlank(message = "annotation may not be blank or null")
    @Size(max = 2000, min = 20)
    private String annotation;

    @NotNull(message = "category may not be null or negative")
    @Positive
    private Long category;

    @NotBlank(message = "description may not be blank or null")
    @Size(max = 7000, min = 20)
    private String description;

    @NotNull(message = "eventDate may not be null or have incorrect format")
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private String eventDate;

    @NotNull(message = "location may not be null")
    private Location location;

    private Boolean paid = false;

    @PositiveOrZero
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotBlank(message = "title may not be null or blank")
    @Size(max = 120, min = 3, message = "title size may not be <3 or  >120")
    private String title;

    @Override
    public String toString() {
        return "NewEventDto{" +
                "id=" + id +
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
