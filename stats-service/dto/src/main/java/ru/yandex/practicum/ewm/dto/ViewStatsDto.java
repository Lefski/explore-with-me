package ru.yandex.practicum.ewm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ViewStatsDto {
    private String app;

    private String uri;

    private Integer hits;

}
