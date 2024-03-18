package ru.practicum.ewm.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.dto.EventFullDto;
import ru.practicum.ewm.event.model.dto.EventInDto;
import ru.practicum.ewm.event.model.dto.EventShortDto;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING,
        imports = {Location.class, LocalDateTime.class, EventState.class})
public interface EventMapper {
    String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "isPaid", source = "eventInDto.paid")
    @Mapping(target = "compilations", expression = "java(null)")
    @Mapping(target = "state", expression = "java(EventState.PENDING)")
    @Mapping(target = "createdOn", expression = "java(LocalDateTime.now())")
    @Mapping(target = "confirmedRequests", expression = "java(new Integer(0))")
    @Mapping(target = "lat", expression = "java(eventInDto.getLocation().getLat())")
    @Mapping(target = "lon", expression = "java(eventInDto.getLocation().getLon())")
    @Mapping(target = "eventDate", expression = "java(LocalDateTime.parse(eventInDto.getEventDate(), FORMATTER))")
    Event mapToEvent(EventInDto eventInDto, User initiator, Category category);

    @Mapping(target = "paid", source = "event.isPaid")
    @Mapping(target = "views", expression = "java(null)")
    @Mapping(target = "location", expression = "java(new Location(event.getLat(), event.getLon()))")
    EventFullDto mapToEventFullDto(Event event);

    @Mapping(target = "paid", source = "event.isPaid")
    @Mapping(target = "views", expression = "java(eventViews.get(event.getId()))")
    @Mapping(target = "location", expression = "java(new Location(event.getLat(), event.getLon()))")
    EventFullDto mapToEventFullDtoWithViews(Event event, Map<Long, Long> eventViews);

    @Mapping(target = "paid", source = "event.isPaid")
    @Mapping(target = "views", expression = "java(eventViews.get(event.getId()))")
    EventShortDto mapToEventShortDto(Event event, Map<Long, Long> eventViews);
}
