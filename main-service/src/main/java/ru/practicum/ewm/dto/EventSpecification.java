package ru.practicum.ewm.dto;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.dto.filter.AdminSearchFilter;
import ru.practicum.ewm.dto.filter.PublicSearchFilter;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EventSpecification {

    public static List<Specification<Event>> adminSearchFilterToSpecification(AdminSearchFilter adminSearchFilter) {
        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add(adminSearchFilter.getUsers() == null
                ? null
                : checkByInitiatorId(adminSearchFilter.getUsers()));
        specifications.add(adminSearchFilter.getStates() == null
                ? null
                : checkByStates(adminSearchFilter.getStates()));
        specifications.add(adminSearchFilter.getCategories() == null
                ? null
                : checkByCategories(adminSearchFilter.getCategories()));
        specifications.add((adminSearchFilter.getRangeStart() == null || adminSearchFilter.getRangeEnd() == null)
                ? null
                : checkByDate(adminSearchFilter.getRangeStart(), adminSearchFilter.getRangeEnd()));
        return specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static List<Specification<Event>> publicSearchFilterToSpecification(PublicSearchFilter filterPublic) {
        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add(checkByStates(List.of(String.valueOf(EventStatus.PUBLISHED))));
        specifications.add(filterPublic.getCategories() == null
                ? null
                : checkByCategories(filterPublic.getCategories()));
        specifications.add(filterPublic.getPaid() == null
                ? null
                : checkByPaid(filterPublic.getPaid()));
        specifications.add((filterPublic.getRangeStart() == null || filterPublic.getRangeEnd() == null)
                ? checkByDate(LocalDateTime.now(), LocalDateTime.now().plusYears(10))
                : checkByDate(filterPublic.getRangeStart(), filterPublic.getRangeEnd()));
        //: checkByAvailableOnly(filterPublic.getOnlyAvailable()));
        //todo check
        specifications.add(null);
        return specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }


    public static Specification<Event> checkByInitiatorId(List<Long> usersId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("initiator").get("id")).value(usersId);
    }

    public static Specification<Event> checkByStates(List<String> states) {
        List<EventStatus> statuses = new ArrayList<>();
        for (String status :
                states) {
            statuses.add(EventStatus.valueOf(status));
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("state")).value(statuses);
    }


    public static Specification<Event> checkByCategories(List<Long> categoryId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("id")).value(categoryId);
    }

    public static Specification<Event> checkByDate(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("eventDate"), rangeStart, rangeEnd);

    }

    public static Specification<Event> checkByPaid(Boolean paid) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paid"), paid);
    }

    public static Specification<Event> checkByAnnotation(String text) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%");
    }

    public static Specification<Event> checkByDescription(String text) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%");
    }

//    public static Specification<Event> checkByAvailableOnly(Boolean onlyAvailable) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("available"), onlyAvailable);
//    } //todo check it
}
