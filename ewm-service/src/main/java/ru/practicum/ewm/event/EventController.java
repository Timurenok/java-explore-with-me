package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.StatisticClient;
import ru.practicum.ewm.dto.StatisticDto;
import ru.practicum.ewm.event.model.dto.*;
import ru.practicum.ewm.request.model.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.model.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.model.dto.RequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.type.descriptor.java.JdbcTimeTypeDescriptor.TIME_FORMAT;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final StatisticClient statisticClient;

    @PostMapping(value = "/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable Long userId, @Valid @RequestBody EventInDto eventInDto) {
        log.info("Saving event {} by user with id {}", eventInDto, userId);
        return eventService.save(userId, eventInDto);
    }

    @PatchMapping(value = "/users/{userId}/events/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @Valid @RequestBody UpdateEventUserRequest eventUpdate) {
        log.info("Updating event with id {} by user with id {} on {}", eventId, userId, eventUpdate);
        return eventService.updateByUser(userId, eventId, eventUpdate);
    }

    @PatchMapping(value = "/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @Valid @RequestBody UpdateEventAdminRequest updateRequest) {
        log.info("Updating event with id {} by admin on {}", eventId, updateRequest);
        return eventService.updateByAdmin(eventId, updateRequest);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto findEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Getting event with id {} by user with id {}", eventId, userId);
        return eventService.findById(userId, eventId);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> findEventsByUserId(@PathVariable Long userId,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Getting events of user with id {}", userId);
        return eventService.findEventsByUserId(userId, from, size);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> findEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<String> states,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = TIME_FORMAT)
                                                LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = TIME_FORMAT)
                                                LocalDateTime rangeEnd,
                                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        EventAdminParam eventAdminParam = new EventAdminParam(users, states, categories, rangeStart, rangeEnd, from,
                size);
        log.info("Getting events by admin");
        return eventService.findEventsByAdmin(eventAdminParam);
    }

    @GetMapping("/events")
    public List<EventShortDto> findEventsByPublic(@RequestParam(required = false) String text,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = TIME_FORMAT)
                                                  LocalDateTime rangeStart,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = TIME_FORMAT)
                                                  LocalDateTime rangeEnd,
                                                  @RequestParam(required = false) Boolean onlyAvailable,
                                                  @RequestParam(required = false) String sort,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size,
                                                  HttpServletRequest request) {
        EventUserParam eventUserParam = new EventUserParam(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size);
        StatisticDto statisticDto = new StatisticDto("ewm-main-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now());
        statisticClient.saveHit(statisticDto);
        log.info("Getting events by public");
        return eventService.findEventsByPublic(eventUserParam, request);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto findPublishedEventById(@PathVariable Long eventId,
                                               HttpServletRequest request) {
        StatisticDto statisticDto = new StatisticDto("ewm-main-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now());
        statisticClient.saveHit(statisticDto);
        log.info("Getting published event with id {}", eventId);
        return eventService.findPublishedEventById(eventId, request);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> findUserEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Getting requests by user with id {} and event with id {}", userId, eventId);
        return eventService.findUserEventRequests(userId, eventId);
    }

    @PatchMapping(value = "/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestsStatus(@PathVariable Long userId,
                                                                    @PathVariable Long eventId,
                                                                    @Valid @RequestBody EventRequestStatusUpdateRequest
                                                                            updateRequest) {
        log.info("Updating request by user with id {} and event with id {} on {}", userId, eventId, updateRequest);
        return eventService.updateEventRequestsStatus(userId, eventId, updateRequest);
    }
}
