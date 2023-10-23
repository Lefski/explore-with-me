package ru.practicum.ewm.dto.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEventAdminRequest extends UpdateEventUserRequest {
}

    /*
    По факту update запрос от администратора содержит все те же поля что и запрос от пользователя. Чтобы разделить их и
    не присылать запросы от пользователя через AdminController сделал класс-наследник
    */
