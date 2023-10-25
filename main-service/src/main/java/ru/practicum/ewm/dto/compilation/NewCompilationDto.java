package ru.practicum.ewm.dto.compilation;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilationDto {

    private HashSet<Long> events = new HashSet<>();

    private Boolean pinned = false;

    @NotBlank
    @Size(min = 1, max = 50, message = "title size may not be <1 or >50")
    private String title;

    @Override
    public String toString() {
        return "NewCompilationDto{" +
                "events=" + events +
                ", pinned=" + pinned +
                ", title='" + title + '\'' +
                '}';
    }
}
