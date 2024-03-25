package ru.practicum.ewm.event.model.dto;

import lombok.*;
import ru.practicum.ewm.event.model.Location;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {

    @Size(min = 20, max = 2000, message = "The length of the annotation must be from 2 to 2000")
    private String annotation;
    @Positive
    private Long category;
    @Size(min = 20, max = 7000, message = "The length of the description must be from 20 to 7000")
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    @Positive
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    @Size(min = 3, max = 120, message = "The length of the title must be from 3 to 120")
    private String title;
}
