package ru.practicum.ewm.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.StatisticDto;
import ru.practicum.ewm.StatisticViewDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository statisticRepository;
    private final StatisticMapper statisticMapper;

    @Override
    @Transactional
    public void saveHit(StatisticDto statisticDto) {
        statisticRepository.save(statisticMapper.statisticDtoToStatistic(statisticDto));
    }

    @Override
    public List<StatisticViewDto> findStatistics(LocalDateTime startTime, LocalDateTime endTime, List<String> uris, Boolean unique) {


        if (uris != null) {
            if (unique) {
                return statisticRepository.findStatisticsByUri(startTime, endTime, uris).stream().map(statistic ->
                        statisticMapper.statisticToStatisticViewDto(statistic, statisticRepository.countUniqueHits(
                                statistic.getUri()))).collect(Collectors.toList());
            }
            return statisticRepository.findStatisticsByUri(startTime, endTime, uris).stream().map(statistic ->
                    statisticMapper.statisticToStatisticViewDto(statistic, statisticRepository.countHits(statistic
                            .getUri()))).collect(Collectors.toList());
        }

        if (unique) {
            return statisticRepository.findStatistics(startTime, endTime).stream().map(statistic -> statisticMapper
                    .statisticToStatisticViewDto(statistic, statisticRepository.countUniqueHits(statistic
                            .getUri()))).collect(Collectors.toList());
        }
        return statisticRepository.findStatistics(startTime, endTime).stream().map(statistic -> statisticMapper
                .statisticToStatisticViewDto(statistic, statisticRepository.countHits(statistic.getUri()))).collect(
                Collectors.toList());
    }
}
