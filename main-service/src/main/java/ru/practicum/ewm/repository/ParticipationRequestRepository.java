package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.participationRequest.ParticipationRequest;
import ru.practicum.ewm.model.participationRequest.ParticipationRequestStatus;

import java.util.List;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    boolean existsByRequester_IdAndEvent_Id(Long userId, Long eventId);

    Long countByEvent_Id(Long eventId);

    List<ParticipationRequest> findAllByRequester_Id(Long userId);

    List<ParticipationRequest> findAllByEvent_Id(Long eventId);

    List<ParticipationRequest> findAllByEvent_IdIn(List<Long> requestIds);

    Long countByEvent_IdAndStatus(Long eventId, ParticipationRequestStatus confirmed);
}
