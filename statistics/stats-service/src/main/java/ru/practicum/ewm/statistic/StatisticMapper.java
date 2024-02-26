package ru.practicum.ewm.statistic;

import org.mapstruct.Mapper;
import ru.practicum.ewm.StatisticDto;
import ru.practicum.ewm.StatisticViewDto;
import ru.practicum.ewm.statistic.model.Statistic;

@Mapper(componentModel = "spring")
public interface StatisticMapper {

    Statistic statisticDtoToStatistic(StatisticDto statisticDto);

    StatisticViewDto statisticToStatisticViewDto(Statistic statistic, long hits);
}