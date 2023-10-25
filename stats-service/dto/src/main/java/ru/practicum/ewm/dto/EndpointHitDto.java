package ru.practicum.ewm.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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

    @Override
    public String toString() {
        return "EndpointHitDto{" +
                "id=" + id +
                ", app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", ip='" + ip + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
