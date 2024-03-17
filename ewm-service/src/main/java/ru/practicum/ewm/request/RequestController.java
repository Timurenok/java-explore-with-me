package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.model.dto.RequestDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addRequest(@PathVariable Long userId, @NotNull @RequestParam Long eventId) {
        return requestService.save(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestDto rejectRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return requestService.update(userId, requestId);
    }

    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> findUserRequests(@PathVariable Long userId) {
        return requestService.findUserRequests(userId);
    }
}
