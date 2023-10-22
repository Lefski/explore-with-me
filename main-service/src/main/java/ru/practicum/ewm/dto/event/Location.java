package ru.practicum.ewm.dto.event;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {

    @NotBlank(message = "Location lat may not be blank or null")
    private Float lat;
    @NotBlank(message = "Location lon may not be blank or null")
    private Float lon;

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
