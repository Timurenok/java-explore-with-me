package ru.practicum.ewm.statistic;

import ru.practicum.ewm.StatisticDto;
import ru.practicum.ewm.StatisticViewDto;

import java.util.List;

public interface StatisticService {

    void saveHit(StatisticDto statisticDto);

    List<StatisticViewDto> findStatistics(String start, String end, List<String> uris, Boolean unique);
}
