package ru.practicum.ewm.dto;


import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "name may not be blank or null")
    @Size(max = 250, min = 2)
    private String name;

    @Email(message = "email may not have incorrect format, be blank or null")
    @NotBlank
    @Size(max = 254, min = 6)
    private String email;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
