package ru.practicum.ewm.statistic;

import org.mapstruct.Mapper;
import ru.practicum.ewm.StatisticDto;
import ru.practicum.ewm.statistic.model.Statistic;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface StatisticMapper {

    Statistic mapToStatistic(StatisticDto statisticDto);
}