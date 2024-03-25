package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.exception.DataConflictException;
import ru.practicum.ewm.exception.UnknownEventException;
import ru.practicum.ewm.exception.UnknownRequestException;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.model.dto.RequestDto;
import ru.practicum.ewm.user.UserService;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final UserService userService;
    private final RequestMapper requestMapper;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public RequestDto save(Long userId, Long eventId) {
        User requester = userService.findById(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new UnknownEventException(
                String.format("Event with id %d does not exist", eventId)));
        List<Request> requests = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (!requests.isEmpty()) {
            throw new DataConflictException(String.format("Request with user id %d and event id %d has already existed",
                    userId, eventId));
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new DataConflictException("Creator of the event can't be a participant");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new DataConflictException("Event must be published, before creating request on it");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new DataConflictException("Can't add request on event, because maximum amount of participants has " +
                    "already confirmed");
        }
        Request request = requestMapper.mapToRequest(requester, event);
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        request = requestRepository.save(request);
        return requestMapper.mapToRequestDto(request);
    }

    @Override
    @Transactional
    public RequestDto update(Long userId, Long requestId) {
        userService.findById(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new UnknownRequestException(
                String.format("Request with id %d does not exist", requestId)));
        request.setStatus(RequestStatus.CANCELED);
        request = requestRepository.save(request);
        return requestMapper.mapToRequestDto(request);
    }

    @Override
    public List<RequestDto> findUserRequests(Long userId) {
        userService.findById(userId);
        return requestRepository.findByRequesterId(userId).stream().map(requestMapper::mapToRequestDto).collect(
                Collectors.toList());
    }
}
