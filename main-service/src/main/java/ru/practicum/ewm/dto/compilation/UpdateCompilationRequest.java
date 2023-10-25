package ru.practicum.ewm.dto.compilation;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompilationRequest {

    private HashSet<Long> events;

    private Boolean pinned;

    @Size(min = 1, max = 50, message = "title size may not be <1 or >50")
    private String title;
}
