package ru.practicum.ewm.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDto {

    @NotEmpty(message = "App's name can't be empty")
    private String app;
    @NotEmpty(message = "URI can't be empty")
    private String uri;
    @NotEmpty(message = "IP can't be empty")
    private String ip;
    @NotNull(message = "Time can't be null")
    private LocalDateTime timestamp;
}
