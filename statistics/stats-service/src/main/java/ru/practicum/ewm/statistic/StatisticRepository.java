package ru.practicum.ewm.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.statistic.model.Statistic;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    List<Statistic> findByTimestampIsAfterAndTimestampIsBeforeOrderByTimestamp(LocalDateTime start,
                                                                               LocalDateTime end);

    List<Statistic> findDistinctByTimestampIsAfterAndTimestampIsBeforeOrderByTimestamp(LocalDateTime start,
                                                                                       LocalDateTime end);

    List<Statistic> findByTimestampIsAfterAndTimestampIsBeforeAndUriInOrderByTimestamp(
            LocalDateTime start, LocalDateTime end, List<String> uris);

    List<Statistic> findDistinctByTimestampIsAfterAndTimestampIsBeforeAndUriInOrderByTimestamp(
            LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT COUNT(ip)," +
            "FROM statistics" +
            "WHERE uri = :uri" +
            "GROUP BY uri")
    long countHits(String uri);
}
