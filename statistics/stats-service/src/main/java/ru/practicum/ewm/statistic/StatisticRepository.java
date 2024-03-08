package ru.practicum.ewm.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.StatisticViewDto;
import ru.practicum.ewm.statistic.model.Statistic;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Query("SELECT new ru.practicum.ewm.StatisticViewDto(s.app, s.uri, COUNT (s.ip))" +
            "FROM Statistic AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT (s.ip) DESC")
    List<StatisticViewDto> findStatisticsByTime(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.StatisticViewDto(s.app, s.uri, COUNT (DISTINCT s.ip))" +
            "FROM Statistic AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT (s.ip) DESC")
    List<StatisticViewDto> findStatisticsByTimeAndUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.StatisticViewDto(s.app, s.uri, COUNT (s.ip))" +
            "FROM Statistic AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT (s.ip) DESC")
    List<StatisticViewDto> findStatisticsByTimeAndUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ewm.StatisticViewDto(s.app, s.uri, COUNT (DISTINCT s.ip))" +
            "FROM Statistic AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT (s.ip) DESC")
    List<StatisticViewDto> findStatisticsByTimeAndUriAndUniqueIp(LocalDateTime start, LocalDateTime end,
                                                                 List<String> uris);
}
