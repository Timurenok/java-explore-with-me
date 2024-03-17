package ru.practicum.ewm.compilation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.dto.CompilationInDto;
import ru.practicum.ewm.event.model.Event;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface CompilationMapper {

    @Mapping(target = "events", source = "events")
    Compilation mapToCompilation(CompilationInDto compilationInDto, List<Event> events);

    CompilationDto mapToCompilationDto(Compilation compilation, Map<Long, Long> views);
}
