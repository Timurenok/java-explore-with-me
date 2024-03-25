package ru.practicum.ewm.compilation;

import ru.practicum.ewm.compilation.model.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.dto.CompilationInDto;
import ru.practicum.ewm.compilation.model.dto.CompilationUpdateRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto save(CompilationInDto compilationDto);

    CompilationDto update(Long compId, CompilationUpdateRequest update);

    CompilationDto findById(Long compId);

    List<CompilationDto> findCompilations(Boolean pinned, Integer from, Integer size);

    void remove(Long compId);
}
