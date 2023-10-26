package ru.practicum.ewm.dto.comment;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCommentDto {

    @NotBlank(message = "comment may not be blank or null")
    private String text;

    @Positive(message = "eventId may not be negative")
    private Long eventId;

}
