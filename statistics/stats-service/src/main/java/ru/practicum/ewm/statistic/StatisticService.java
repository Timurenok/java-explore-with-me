package ru.practicum.ewm.statistic;

import ru.practicum.ewm.StatisticDto;
import ru.practicum.ewm.StatisticViewDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    void saveHit(StatisticDto statisticDto);

    List<StatisticViewDto> findStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
