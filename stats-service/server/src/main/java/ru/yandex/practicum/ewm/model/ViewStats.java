package ru.yandex.practicum.ewm.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ViewStats {
    private String app;

    private String uri;

    private Integer hits;


}
