package ru.practicum.ewm.event.model.dto;

import lombok.*;
import ru.practicum.ewm.category.model.dto.CategoryDto;
import ru.practicum.ewm.user.model.dto.UserShortDto;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {

    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
