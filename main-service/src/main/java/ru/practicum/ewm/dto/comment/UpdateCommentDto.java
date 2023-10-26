package ru.practicum.ewm.dto.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCommentDto {

    @NotBlank(message = "comment update may not be blank or null")
    private String text;

}
