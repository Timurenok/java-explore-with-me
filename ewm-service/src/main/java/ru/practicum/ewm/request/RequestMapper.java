package ru.practicum.ewm.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.model.dto.RequestDto;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING,
        imports = {LocalDateTime.class, RequestStatus.class})
public interface RequestMapper {

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(RequestStatus.PENDING)")
    Request mapToRequest(User requester, Event event);

    @Mapping(target = "requester", source = "request.requester.id")
    @Mapping(target = "event", source = "request.event.id")
    RequestDto mapToRequestDto(Request request);
}
