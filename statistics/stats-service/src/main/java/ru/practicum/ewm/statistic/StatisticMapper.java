package ru.practicum.ewm.statistic;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.StatisticDto;
import ru.practicum.ewm.statistic.model.Statistic;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface StatisticMapper {

    @Mapping(target = "id", expression = "java(null)")
    Statistic mapToStatistic(StatisticDto statisticDto);
}