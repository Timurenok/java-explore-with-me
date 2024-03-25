package ru.practicum.ewm;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StatisticViewDto {

    private String app;
    private String uri;
    private Long hits;
}