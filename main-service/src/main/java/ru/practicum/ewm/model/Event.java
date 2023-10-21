package ru.practicum.ewm.model;


import lombok.*;

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
    @Column(name = "annotation")
    String annotation;
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    Category category;
    @Column(name = "description")
    String description;
    @Column(name = "eventDate")
    LocalDateTime eventDate;
    @Column(name = "lat")
    Float lat;
    @Column(name = "lon")
    Float lon;
    @Column(name = "paid")
    Boolean paid;
    @Column(name = "participantsLimit")
    Integer participantsLimit;
    @Column(name = "requestModeration")
    Boolean requestModeration;
    @Column(name = "title")
    String title;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
