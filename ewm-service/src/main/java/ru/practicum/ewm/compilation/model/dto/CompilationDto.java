package ru.practicum.ewm.compilation.model.dto;

import lombok.*;
import ru.practicum.ewm.event.model.dto.EventShortDto;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
