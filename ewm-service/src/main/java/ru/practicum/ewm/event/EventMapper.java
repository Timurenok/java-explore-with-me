package ru.practicum.ewm.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.dto.EventFullDto;
import ru.practicum.ewm.event.model.dto.EventInDto;
import ru.practicum.ewm.event.model.dto.EventShortDto;
import ru.practicum.ewm.user.model.User;

import java.util.Map;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "category", source = "category")
    Event mapToEvent(EventInDto eventInDto, User initiator, Category category);

    EventFullDto mapToEventFullDto(Event event);

    EventFullDto mapToEventFullDtoWithViews(Event event, Map<Long, Long> eventViews);

    EventShortDto mapToEventShortDto(Event event, Map<Long, Long> eventViews);
}
