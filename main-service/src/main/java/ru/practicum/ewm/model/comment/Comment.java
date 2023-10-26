package ru.practicum.ewm.model.comment;

import lombok.*;
import org.hibernate.annotations.GenerationTime;
import ru.practicum.ewm.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    @JoinColumn(name = "creator_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private User creator;

    @Column(name = "comment_text")
    private String text;

    @Column(name = "created")
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private LocalDateTime created;

    @Column(name = "updated")
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private LocalDateTime updated;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CommentStatus status;

}
