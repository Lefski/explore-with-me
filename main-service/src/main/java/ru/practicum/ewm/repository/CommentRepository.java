package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.comment.Comment;
import ru.practicum.ewm.model.comment.CommentStatus;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCreator_Id(Long userId);

    List<Comment> findAllByCreator_IdAndStatusIsNot(Long userId, CommentStatus commentStatus);

    @Query(value = "SELECT * FROM comments WHERE event_id = :eventId", nativeQuery = true)
    List<Comment> findCommentsByEventId(Long eventId);
}
