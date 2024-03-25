package ru.practicum.ewm.compilation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.dto.CompilationInDto;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.model.Event;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING, uses = EventMapper.class,
        imports = Collectors.class)
public abstract class CompilationMapper {

    @Autowired
    protected EventMapper eventMapper;

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "isPinned", source = "compilationInDto.pinned")
    @Mapping(target = "events", source = "events")
    public abstract Compilation mapToCompilation(CompilationInDto compilationInDto, List<Event> events);

    @Mapping(target = "pinned", source = "compilation.isPinned")
    @Mapping(target = "events",
            expression = "java(compilation.getEvents().stream().map(event -> " +
                    "eventMapper.mapToEventShortDto(event, views)).collect(Collectors.toList()))")
    public abstract CompilationDto mapToCompilationDto(Compilation compilation, Map<Long, Long> views);
}
