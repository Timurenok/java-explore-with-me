package ru.practicum.ewm.request;

import ru.practicum.ewm.request.model.dto.RequestDto;

import java.util.List;

public interface RequestService {

    RequestDto save(Long userId, Long eventId);

    RequestDto update(Long userId, Long requestId);

    List<RequestDto> findUserRequests(Long userId);
}
