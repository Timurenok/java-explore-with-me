package ru.practicum.ewm.statistic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.StatisticDto;
import ru.practicum.ewm.StatisticViewDto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @PostMapping(value = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@Valid @RequestBody StatisticDto statisticDto) {
        statisticService.saveHit(statisticDto);
    }

    @GetMapping("/stats")
    public List<StatisticViewDto> findStatistics(@NotEmpty @RequestParam String start,
                                                 @NotEmpty @RequestParam String end,
                                                 @RequestParam(required = false) List<String> uris,
                                                 @RequestParam(value = "unique", defaultValue = "false") String unique) {
        Boolean uniqueParam = Boolean.valueOf(unique);
        return statisticService.findStatistics(start, end, uris, uniqueParam);
    }
}
