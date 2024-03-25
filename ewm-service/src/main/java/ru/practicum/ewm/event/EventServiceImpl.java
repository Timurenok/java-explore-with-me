package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.CategoryService;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.event.model.dto.*;
import ru.practicum.ewm.exception.DataConflictException;
import ru.practicum.ewm.exception.InvalidDataException;
import ru.practicum.ewm.exception.InvalidRequestException;
import ru.practicum.ewm.exception.UnknownEventException;
import ru.practicum.ewm.request.RequestMapper;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.model.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.model.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.model.dto.RequestDto;
import ru.practicum.ewm.user.UserService;
import ru.practicum.ewm.user.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.EventStatService.FORMATTER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final UserService userService;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final EventStatService eventStatService;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public EventFullDto save(Long userId, EventInDto eventInDto) {
        User user = userService.findById(userId);
        Category category = categoryMapper.mapToCategoryFromDto(categoryService.findById(eventInDto.getCategory()));
        Event event = eventMapper.mapToEvent(eventInDto, user, category);
        checkEventTimeByUser(event.getEventDate());
        event = eventRepository.save(event);
        EventFullDto eventFullDto = eventMapper.mapToEventFullDto(event);
        eventFullDto.setViews(0L);
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto updateByUser(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        userService.findById(userId);
        Event oldEvent = findEventById(eventId);
        if (oldEvent.getState().equals(EventState.PUBLISHED)) {
            throw new DataConflictException("You can change only cancelled events or events in state of waiting of " +
                    "moderation");
        }
        if (updateRequest.getEventDate() != null) {
            LocalDateTime updateEventTime = LocalDateTime.parse(updateRequest.getEventDate(), FORMATTER);
            checkEventTimeByUser(updateEventTime);
        }
        if (updateRequest.getStateAction() != null) {
            updateEventByUserStateAction(oldEvent, updateRequest);
        }
        if (updateRequest.getAnnotation() != null) {
            oldEvent.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            Category category = categoryMapper.mapToCategoryFromDto(categoryService.findById(
                    updateRequest.getCategory()));
            oldEvent.setCategory(category);
        }
        if (updateRequest.getDescription() != null) {
            oldEvent.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getLocation() != null) {
            oldEvent.setLat(updateRequest.getLocation().getLat());
            oldEvent.setLon(updateRequest.getLocation().getLon());
        }
        if (updateRequest.getPaid() != null) {
            oldEvent.setIsPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getRequestModeration() != null) {
            oldEvent.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getTitle() != null) {
            oldEvent.setTitle(updateRequest.getTitle());
        }
        Event updatedEvent = eventRepository.save(oldEvent);
        Map<Long, Long> views = eventStatService.findEventsViews(List.of(eventId));
        return eventMapper.mapToEventFullDtoWithViews(updatedEvent, views);
    }

    @Override
    @Transactional
    public EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest updateRequest) {
        Event oldEvent = findEventById(eventId);
        if (updateRequest.getEventDate() != null) {
            LocalDateTime updateTime = LocalDateTime.parse(updateRequest.getEventDate(), FORMATTER);
            checkEventTimeByAdmin(updateTime);
        }
        if (updateRequest.getStateAction() != null) {
            checkEventState(oldEvent.getState());
            updateEventByAdminStateAction(oldEvent, updateRequest);
        }
        if (updateRequest.getAnnotation() != null) {
            oldEvent.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            Category category = categoryMapper.mapToCategoryFromDto(categoryService.findById(updateRequest
                    .getCategory()));
            oldEvent.setCategory(category);
        }
        if (updateRequest.getDescription() != null) {
            oldEvent.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getLocation() != null) {
            oldEvent.setLat(updateRequest.getLocation().getLat());
            oldEvent.setLon(updateRequest.getLocation().getLon());
        }
        if (updateRequest.getPaid() != null) {
            oldEvent.setIsPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getRequestModeration() != null) {
            oldEvent.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getTitle() != null) {
            oldEvent.setTitle(updateRequest.getTitle());
        }
        Event updated = eventRepository.save(oldEvent);
        Map<Long, Long> views = eventStatService.findEventsViews(List.of(eventId));
        return eventMapper.mapToEventFullDtoWithViews(updated, views);
    }

    @Override
    public EventFullDto findById(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(userId, eventId).orElseThrow(() ->
                new UnknownEventException(String.format("Event with id %s does not exist", eventId)));
        Map<Long, Long> views = eventStatService.findEventsViews(List.of(eventId));
        return eventMapper.mapToEventFullDtoWithViews(event, views);
    }

    @Override
    public List<EventShortDto> findEventsByUserId(Long userId, Integer from, Integer size) {
        userService.findById(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findEventsByInitiatorIdOrderByIdDesc(userId, pageable).getContent();
        Map<Long, Long> views = eventStatService.findEventsViews(events.stream().map(Event::getId).collect(
                Collectors.toList()));
        return events.stream().map(event -> eventMapper.mapToEventShortDto(event, views)).collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> findEventsByAdmin(EventAdminParam eventAdminParam) {
        Pageable pageable = PageRequest.of(eventAdminParam.getFrom() / eventAdminParam.getSize(),
                eventAdminParam.getSize());
        Specification<Event> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (eventAdminParam.getUsers() != null) {
                CriteriaBuilder.In<Long> usersClause = criteriaBuilder.in(root.get("initiator"));
                for (Long user : eventAdminParam.getUsers()) {
                    usersClause.value(user);
                }
                predicates.add(usersClause);
            }
            if (eventAdminParam.getStates() != null) {
                List<EventState> states = findEventStates(eventAdminParam.getStates());
                CriteriaBuilder.In<EventState> statesClause = criteriaBuilder.in(root.get("state"));
                for (EventState state : states) {
                    statesClause.value(state);
                }
                predicates.add(statesClause);
            }
            if (eventAdminParam.getCategories() != null) {
                CriteriaBuilder.In<Long> categoriesClause = criteriaBuilder.in(root.get("category"));
                for (Long category : eventAdminParam.getCategories()) {
                    categoriesClause.value(category);
                }
                predicates.add(categoriesClause);
            }
            if (eventAdminParam.getRangeStart() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("eventDate"), eventAdminParam.getRangeStart()));
            }
            if (eventAdminParam.getRangeEnd() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("eventDate"), eventAdminParam.getRangeEnd()));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        });
        List<Event> events = eventRepository.findAll(specification, pageable).getContent();
        Map<Long, Long> views = eventStatService.findEventsViews(events.stream().map(Event::getId).collect(
                Collectors.toList()));
        return events.stream().map(event -> eventMapper.mapToEventFullDtoWithViews(event, views)).collect(
                Collectors.toList());
    }

    @Override
    public List<EventShortDto> findEventsByPublic(EventUserParam eventUserParam, HttpServletRequest request) {
        Sort sort = findEventSort(eventUserParam.getSort());
        Pageable pageable = PageRequest.of(eventUserParam.getFrom() / eventUserParam.getSize(),
                eventUserParam.getSize(), sort);
        LocalDateTime checkedRangeStart = checkRangeTime(eventUserParam.getRangeStart(), eventUserParam.getRangeEnd());
        Specification<Event> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("state"), EventState.PUBLISHED));
            if (eventUserParam.getText() != null) {
                predicates.add(criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")),
                                "%" + eventUserParam.getText().toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                                "%" + eventUserParam.getText().toLowerCase() + "%")));
            }
            if (eventUserParam.getCategories() != null) {
                CriteriaBuilder.In<Long> categoriesClause = criteriaBuilder.in(root.get("category"));
                for (Long category : eventUserParam.getCategories()) {
                    categoriesClause.value(category);
                }
                predicates.add(categoriesClause);
            }
            if (eventUserParam.getPaid() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isPaid"), eventUserParam.getPaid()));
            }
            predicates.add(criteriaBuilder.greaterThan(root.get("eventDate"), checkedRangeStart));
            if (eventUserParam.getRangeEnd() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("eventDate"), eventUserParam.getRangeEnd()));
            }
            if (eventUserParam.getOnlyAvailable() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("confirmedRequests"), root.get("participantLimit")));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        });
        List<Event> events = eventRepository.findAll(specification, pageable).getContent();
        Map<Long, Long> views = eventStatService.findEventsViews(events.stream().map(Event::getId).collect(
                Collectors.toList()));
        return events.stream().map(event -> eventMapper.mapToEventShortDto(event, views)).collect(
                Collectors.toList());
    }

    @Override
    public EventFullDto findPublishedEventById(Long eventId, HttpServletRequest request) {
        Map<Long, Long> views = eventStatService.findEventsViews(List.of(eventId));
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED).orElseThrow(() ->
                new UnknownEventException(String.format("Event with id %d does not exist", eventId)));
        return eventMapper.mapToEventFullDtoWithViews(event, views);
    }

    @Override
    public List<RequestDto> findUserEventRequests(Long userId, Long eventId) {
        userService.findById(userId);
        findEventById(eventId);
        List<Request> eventRequests = requestRepository.findByEventId(eventId);
        return eventRequests.stream().map(requestMapper::mapToRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventRequestsStatus(Long userId, Long eventId,
                                                                    EventRequestStatusUpdateRequest updateRequest) {
        int requestsCount = updateRequest.getRequestIds().size();
        userService.findById(userId);
        Event event = findEventById(eventId);
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();
        RequestStatus status = RequestStatus.valueOf(updateRequest.getStatus());
        List<Request> requests = requestRepository.findByIdIn(updateRequest.getRequestIds());

        if (!Objects.equals(userId, event.getInitiator().getId())) {
            throw new InvalidRequestException(String.format("User with id %d does not have the event with id %d",
                    userId, eventId));
        }
        for (Request request : requests) {
            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new DataConflictException("You can only change status for waiting events");
            }
        }
        switch (status) {
            case CONFIRMED:
                if (event.getParticipantLimit() == 0 || !event.getRequestModeration()
                        || event.getParticipantLimit() > event.getConfirmedRequests() + requestsCount) {
                    requests.forEach(request -> request.setStatus(RequestStatus.CONFIRMED));
                    event.setConfirmedRequests(event.getConfirmedRequests() + requestsCount);
                    confirmed.addAll(requests);
                } else if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
                    throw new DataConflictException("The limit of requests in the event has been reached");
                } else {
                    for (Request request : requests) {
                        if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                            request.setStatus(RequestStatus.CONFIRMED);
                            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                            confirmed.add(request);
                        } else {
                            request.setStatus(RequestStatus.REJECTED);
                            rejected.add(request);
                        }
                    }
                }
                break;
            case REJECTED:
                requests.forEach(request -> request.setStatus(RequestStatus.REJECTED));
                rejected.addAll(requests);
        }
        eventRepository.save(event);
        requestRepository.saveAll(requests);
        List<RequestDto> confirmedRequests = confirmed.stream().map(requestMapper::mapToRequestDto).collect(
                Collectors.toList());
        List<RequestDto> rejectedRequests = rejected.stream().map(requestMapper::mapToRequestDto).collect(
                Collectors.toList());
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    @Override
    public List<Event> findEventsByIds(List<Long> ids) {
        return eventRepository.findByIdIn(ids);
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new UnknownEventException(String.format(
                "Event with id %d does not exist", eventId)));
    }

    private void checkEventTimeByUser(LocalDateTime eventTime) {
        if (eventTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidRequestException("Date and time which are planned on event can't be earlier than 2 hours");
        }
    }

    private void checkEventTimeByAdmin(LocalDateTime eventTime) {
        if (eventTime.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new InvalidRequestException("Starting date of the event can't be earlier than 1 hour");
        }
    }

    private LocalDateTime checkRangeTime(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new InvalidDataException("Starting time must be earlier than end");
        } else return Objects.requireNonNullElseGet(rangeStart, LocalDateTime::now);
    }

    private void checkEventState(EventState state) {
        if (!state.equals(EventState.PENDING)) {
            throw new DataConflictException("Event is not in state of waiting");
        }
    }

    private void updateEventByUserStateAction(Event oldEvent, UpdateEventUserRequest eventUpdate) {
        UserEventStateAction stateAction;
        try {
            stateAction = UserEventStateAction.valueOf(eventUpdate.getStateAction());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(String.format("Unknown param: %s", eventUpdate.getStateAction()));
        }
        switch (stateAction) {
            case SEND_TO_REVIEW:
                oldEvent.setState(EventState.PENDING);
                break;
            case CANCEL_REVIEW:
                oldEvent.setState(EventState.CANCELED);
                break;
            default:
                throw new InvalidRequestException(String.format("Unknown param: %s", eventUpdate.getStateAction()));
        }
    }

    private void updateEventByAdminStateAction(Event oldEvent, UpdateEventAdminRequest eventUpdate) {
        AdminEventStateAction stateAction;
        try {
            stateAction = AdminEventStateAction.valueOf(eventUpdate.getStateAction());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException(String.format("Unknown param: %s", eventUpdate.getStateAction()));
        }
        switch (stateAction) {
            case REJECT_EVENT:
                if (oldEvent.getState().equals(EventState.PUBLISHED)) {
                    throw new InvalidRequestException("You can't reject already published event");
                }
                oldEvent.setState(EventState.CANCELED);
                break;
            case PUBLISH_EVENT:
                if (!oldEvent.getState().equals(EventState.PENDING)) {
                    throw new InvalidRequestException("You can publish only events in state of waiting");
                }
                oldEvent.setState(EventState.PUBLISHED);
                oldEvent.setPublishedOn(LocalDateTime.now());
                break;
            default:
                throw new InvalidRequestException(String.format("Unknown param: %s", eventUpdate.getStateAction()));
        }
    }

    private Sort findEventSort(String eventSort) {
        EventSort sort;
        if (eventSort == null) {
            return Sort.by("id");
        }
        try {
            sort = EventSort.valueOf(eventSort);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid type of sorting of events");
        }
        switch (sort) {
            case EVENT_DATE:
                return Sort.by("eventDate");
            case VIEWS:
                return Sort.by("views");
            default:
                throw new InvalidRequestException("Invalid type of sorting of events");
        }
    }

    private List<EventState> findEventStates(List<String> states) {
        EventState eventState;
        List<EventState> eventStates = new ArrayList<>();
        try {
            for (String state : states) {
                eventState = EventState.valueOf(state);
                eventStates.add(eventState);
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Unknown param of state of the event");
        }
        return eventStates;
    }
}
