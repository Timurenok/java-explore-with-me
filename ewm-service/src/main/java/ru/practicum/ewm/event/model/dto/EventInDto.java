package ru.practicum.ewm.event.model.dto;

import lombok.*;
import ru.practicum.ewm.event.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventInDto {

    @NotNull
    @NotBlank
    @Size(min = 20, max = 2000, message = "The length of the annotation must be from 2 to 2000")
    private String annotation;
    @NotNull
    private Long category;
    @NotNull
    @NotBlank
    @Size(min = 20, max = 7000, message = "The length of the description must be from 20 to 7000")
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotNull
    @Size(min = 3, max = 120, message = "The length of the title must be from 3 to 120")
    private String title;
}
