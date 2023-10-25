package ru.practicum.ewm.dto.filter;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminSearchFilter {

    private List<Long> users;

    private List<String> states;

    private List<Long> categories;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    @Override
    public String toString() {
        return "AdminSearchFilter{" +
                "users=" + users +
                ", states=" + states +
                ", categories=" + categories +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                '}';
    }
}
