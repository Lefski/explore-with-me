package ru.practicum.ewm.dto.participationRequest;


import lombok.*;
import ru.practicum.ewm.model.participationRequest.ParticipationRequestStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationRequestDto {


    private Long id;

    @NotNull(message = "status may not be null")
    private ParticipationRequestStatus status;

    @Positive(message = "requester id may not be negative or zero")
    @NotNull(message = "requester id may not be null")
    private Long requester;

    @Positive(message = "event id may not be negative or zero")
    @NotNull(message = "event id may not be null")
    private Long event;

    private String created;

    @Override
    public String toString() {
        return "ParticipationRequestDto{" +
                "id=" + id +
                ", status=" + status +
                ", requester=" + requester +
                ", event=" + event +
                ", created='" + created + '\'' +
                '}';
    }
}
