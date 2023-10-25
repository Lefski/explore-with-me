package ru.practicum.ewm.dto.participationRequest;


import lombok.*;
import ru.practicum.ewm.model.participationRequest.ParticipationRequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequestStatusUpdateRequest {

    @NotNull
    List<Long> requestIds;

    @NotNull
    ParticipationRequestStatus status;
}
