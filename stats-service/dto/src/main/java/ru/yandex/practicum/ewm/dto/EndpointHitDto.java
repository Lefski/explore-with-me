package ru.yandex.practicum.ewm.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EndpointHitDto {
    private Long id;

    @NotBlank(message = "app may not be blank or null")
    private String app;

    @NotBlank(message = "uri may not be blank or null")
    private String uri;

    @NotBlank(message = "ip may not be blank or null")
    private String ip;

    @NotBlank(message = "app may not be blank or null")
    private String timestamp;
}
