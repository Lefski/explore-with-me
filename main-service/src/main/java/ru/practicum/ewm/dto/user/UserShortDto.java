package ru.practicum.ewm.dto.user;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserShortDto {

    private Long id;

    private String name;

    @Override
    public String toString() {
        return "UserShortDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
