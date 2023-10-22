package ru.practicum.ewm.model.participationRequest;


import lombok.*;
import org.hibernate.annotations.GenerationTime;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "participation_requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ParticipationRequestStatus status;

    @JoinColumn(name = "requester_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private User requester;

    @JoinColumn(name = "event_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Event event;

    @Column(name = "created")
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private LocalDateTime created;

}
