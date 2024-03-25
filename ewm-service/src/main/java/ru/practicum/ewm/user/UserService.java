package ru.practicum.ewm.user;

import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.dto.UserInDto;
import ru.practicum.ewm.user.model.dto.UserOutDto;

import java.util.List;

public interface UserService {

    UserOutDto save(UserInDto userInDto);

    User findById(Long userId);

    void remove(Long userId);

    List<UserOutDto> findUsers(List<Long> ids, Integer from, Integer size);
}
