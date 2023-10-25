package ru.practicum.ewm.dto.filter;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicSearchFilter {

    private String text;

    private List<Long> categories;

    private Boolean paid;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private Boolean onlyAvailable;

    private String sort;

    @Override
    public String toString() {
        return "PublicSearchFilter{" +
                "text='" + text + '\'' +
                ", categories=" + categories +
                ", paid=" + paid +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                ", onlyAvailable=" + onlyAvailable +
                ", sort='" + sort + '\'' +
                '}';
    }
}
