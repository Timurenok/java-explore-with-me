package ru.practicum.ewm.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.StatisticViewDto;
import ru.practicum.ewm.statistic.model.Statistic;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    List<StatisticViewDto> findByTimestampIsAfterAndTimestampIsBeforeOrderByTimestamp(LocalDateTime start,
                                                                                      LocalDateTime end);

    List<StatisticViewDto> findDistinctByTimestampIsAfterAndTimestampIsBeforeOrderByTimestamp(LocalDateTime start,
                                                                                              LocalDateTime end);

    List<StatisticViewDto> findByTimestampIsAfterAndTimestampIsBeforeAndUriInOrderByTimestamp(
            LocalDateTime start, LocalDateTime end, List<String> uris);

    List<StatisticViewDto> findDistinctByTimestampIsAfterAndTimestampIsBeforeAndUriInOrderByTimestamp(
            LocalDateTime start, LocalDateTime end, List<String> uris);
}
