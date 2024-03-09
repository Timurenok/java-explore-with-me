package ru.practicum.ewm.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.StatisticDto;
import ru.practicum.ewm.StatisticViewDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository statisticRepository;
    private final StatisticMapper statisticMapper;

    @Override
    @Transactional
    public void saveHit(StatisticDto statisticDto) {
        statisticRepository.save(statisticMapper.mapToStatistic(statisticDto));
    }

    @Override
    public List<StatisticViewDto> findStatistics(LocalDateTime start, LocalDateTime end, List<String> uris,
                                                 Boolean unique) {
        if (uris != null) {
            if (unique) {
                return statisticRepository.findStatisticsByTimeAndUriAndUniqueIp(start, end, uris);
            }
            return statisticRepository.findStatisticsByTimeAndUri(start, end, uris);
        }

        if (unique) {
            return statisticRepository.findStatisticsByTimeAndUniqueIp(start, end);
        }
        return statisticRepository.findStatisticsByTime(start, end);
    }
}
