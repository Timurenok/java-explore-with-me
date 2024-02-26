package ru.practicum.ewm.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.StatisticDto;
import ru.practicum.ewm.StatisticViewDto;
import ru.practicum.ewm.exception.InvalidStatisticException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticServiceImpl implements StatisticService {
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final StatisticRepository statisticRepository;
    private final StatisticMapper statisticMapper;

    @Override
    @Transactional
    public void saveHit(StatisticDto statisticDto) {
        statisticRepository.save(statisticMapper.statisticDtoToStatistic(statisticDto));
    }

    @Override
    public List<StatisticViewDto> findStatistics(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startTime = parseTimeParam(start);
        LocalDateTime endTime = parseTimeParam(end);

        if (uris != null) {
            if (unique) {
                return statisticRepository.findByTimestampIsAfterAndTimestampIsBeforeAndUriInOrderByTimestamp(
                                startTime, endTime, uris).stream().map(statistic -> statisticMapper
                                .statisticToStatisticViewDto(statistic, statisticRepository.countUniqueHits(statistic
                                        .getUri()))).collect(Collectors.toList());
            }
            return statisticRepository.findByTimestampIsAfterAndTimestampIsBeforeAndUriInOrderByTimestamp(startTime,
                    endTime, uris).stream().map(statistic -> statisticMapper.statisticToStatisticViewDto(statistic,
                    statisticRepository.countHits(statistic.getUri()))).collect(Collectors.toList());
        }

        if (unique) {
            return statisticRepository.findByTimestampIsAfterAndTimestampIsBeforeOrderByTimestamp(startTime,
                    endTime).stream().map(statistic -> statisticMapper.statisticToStatisticViewDto(statistic,
                    statisticRepository.countUniqueHits(statistic.getUri()))).collect(Collectors.toList());
        }
        return statisticRepository.findByTimestampIsAfterAndTimestampIsBeforeOrderByTimestamp(startTime,
                endTime).stream().map(statistic -> statisticMapper.statisticToStatisticViewDto(statistic,
                statisticRepository.countHits(statistic.getUri()))).collect(Collectors.toList());
    }

    private LocalDateTime parseTimeParam(String time) {
        try {
            return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(TIME_FORMAT));
        } catch (DateTimeParseException e) {
            throw new InvalidStatisticException("Invalid time format");
        }
    }
}
