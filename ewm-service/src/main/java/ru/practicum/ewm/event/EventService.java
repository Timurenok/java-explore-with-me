package ru.practicum.ewm.event;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.dto.*;
import ru.practicum.ewm.request.model.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.model.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.model.dto.RequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    EventFullDto save(Long userId, EventInDto newEventDto);

    EventFullDto updateByUser(Long userId, Long eventId, UpdateEventUserRequest eventUpdate);

    EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest eventUpdate);

    EventFullDto findById(Long userId, Long eventId);

    List<EventShortDto> findEventsByUserId(Long userId, Integer from, Integer size);

    List<EventFullDto> findEventsByAdmin(EventAdminParam eventAdminParam);

    List<EventShortDto> findEventsByPublic(EventUserParam eventUserParam, HttpServletRequest request);

    EventFullDto findPublishedEventById(Long eventId, HttpServletRequest request);

    List<RequestDto> findUserEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventRequestsStatus(Long userId, Long eventId,
                                                             EventRequestStatusUpdateRequest request);

    List<Event> findEventsByIds(List<Long> ids);
}
