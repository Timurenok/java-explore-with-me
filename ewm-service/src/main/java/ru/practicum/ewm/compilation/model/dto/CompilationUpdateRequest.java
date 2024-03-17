package ru.practicum.ewm.compilation.model.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompilationUpdateRequest {

    private List<Long> events;
    private Boolean pinned;
    @Size(min = 1, max = 50, message = "The length of the title must be from 1 to 50")
    private String title;
}
