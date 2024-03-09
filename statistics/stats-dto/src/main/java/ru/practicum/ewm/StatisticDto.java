package ru.practicum.ewm;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @NotEmpty(message = "App's name can't be empty")
    private String app;
    @NotEmpty(message = "URI can't be empty")
    private String uri;
    @NotEmpty(message = "IP can't be empty")
    private String ip;
    @NotNull(message = "Time can't be null")
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime timestamp;
}
