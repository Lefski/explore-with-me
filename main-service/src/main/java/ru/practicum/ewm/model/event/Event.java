package ru.practicum.ewm.model.event;


import lombok.*;
import org.hibernate.annotations.GenerationTime;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "events")
public class Event {
    //todo: доделать статус публикации, статус отмена только из ожидания
    @Enumerated(EnumType.STRING)
    private EventStatus state;

    @Column(name = "annotation")
    private String annotation;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Category category;

    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private LocalDateTime createdOn;

    @JoinColumn(name = "initiator_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private User initiator;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lon")
    private Float lon;
    /*
     Тут lat+lon наверное можно было вынести в отдельную таблицу Location, чтобы не терять третью нормальную форму,
     но по факту с точки зрения уменьшения количества запросов в бд есть смысл просто сделать два поля, т.к. они нигде
     кроме event, не используются. Плюс, это мне упрощает логику
     */

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participants_limit")
    private Integer participantsLimit;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "title")
    private String title;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public String toString() {
        return "Event{" +
                "state=" + state +
                ", annotation='" + annotation + '\'' +
                ", category=" + category +
                ", createdOn=" + createdOn +
                ", initiator=" + initiator +
                ", publishedOn=" + publishedOn +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", lat=" + lat +
                ", lon=" + lon +
                ", paid=" + paid +
                ", participantsLimit=" + participantsLimit +
                ", requestModeration=" + requestModeration +
                ", title='" + title + '\'' +
                ", id=" + id +
                '}';
    }
}
