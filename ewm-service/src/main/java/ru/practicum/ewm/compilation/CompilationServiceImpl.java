package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.dto.CompilationInDto;
import ru.practicum.ewm.compilation.model.dto.CompilationUpdateRequest;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.EventStatService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.UnknownCompilationException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final EventService eventService;
    private final EventStatService statService;
    private final CompilationMapper compilationMapper;
    private final CompilationRepository compilationRepository;

    @Override
    @Transactional
    public CompilationDto save(CompilationInDto compilationDto) {
        List<Event> events;
        Map<Long, Long> views = new HashMap<>();
        if (compilationDto.getEvents() != null) {
            events = eventService.findEventsByIds(compilationDto.getEvents());
            views = statService.findEventsViews(events.stream().map(Event::getId).collect(Collectors.toList()));
        } else {
            events = new ArrayList<>();
        }
        Compilation compilation = compilationMapper.mapToCompilation(compilationDto, events);
        compilation = compilationRepository.save(compilation);
        return compilationMapper.mapToCompilationDto(compilation, views);
    }

    @Override
    @Transactional
    public CompilationDto update(Long compilationId, CompilationUpdateRequest updateRequest) {
        Compilation oldCompilation = compilationRepository.findById(compilationId).orElseThrow(() ->
                new UnknownCompilationException(String.format("Compilation with id %d does not exist", compilationId)));
        Map<Long, Long> views = new HashMap<>();
        if (updateRequest.getEvents() != null && !updateRequest.getEvents().isEmpty()) {
            List<Event> events = eventService.findEventsByIds(updateRequest.getEvents());
            oldCompilation.setEvents(events);
            views = statService.findEventsViews(updateRequest.getEvents());
        }
        if (updateRequest.getPinned() != null) {
            oldCompilation.setIsPinned(updateRequest.getPinned());
        }
        if (updateRequest.getTitle() != null) {
            oldCompilation.setTitle(updateRequest.getTitle());
        }
        return compilationMapper.mapToCompilationDto(compilationRepository.save(oldCompilation), views);
    }

    @Override
    public CompilationDto findById(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(() ->
                new UnknownCompilationException(String.format("Compilation with id %d does not exist", compilationId)));
        List<Long> events = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());
        Map<Long, Long> views = statService.findEventsViews(events);
        return compilationMapper.mapToCompilationDto(compilation, views);
    }

    @Override
    public List<CompilationDto> findCompilations(Boolean pinned, Integer from, Integer size) {
        Set<Event> eventSet = new HashSet<>();
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilations = compilationRepository.findAllByIsPinned(pinned, pageable);
        for (Compilation compilation : compilations) {
            eventSet.addAll(compilation.getEvents());
        }
        List<Long> eventIds = eventSet.stream().map(Event::getId).collect(Collectors.toList());
        Map<Long, Long> views = statService.findEventsViews(eventIds);
        return compilations.stream().map(compilation -> compilationMapper.mapToCompilationDto(compilation, views))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void remove(Long compId) {
        if (compilationRepository.existsById(compId)) {
            compilationRepository.deleteById(compId);
        }
    }
}
