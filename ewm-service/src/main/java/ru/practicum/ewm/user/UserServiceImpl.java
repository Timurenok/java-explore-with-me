package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.InvalidDataException;
import ru.practicum.ewm.exception.UnknownUserException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.dto.UserInDto;
import ru.practicum.ewm.user.model.dto.UserOutDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserOutDto save(UserInDto userInDto) {
        User user = userMapper.mapToUser(userInDto);
        return userMapper.mapToUserOutDto(userRepository.save(user));
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UnknownUserException(
                String.format("User with id %d does not exist", userId)));
    }

    @Override
    @Transactional
    public void remove(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new InvalidDataException(String.format("User with id %d does not exist", userId));
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserOutDto> findUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageRequest = PageRequest.of(from / size, size);
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(pageRequest).getContent().stream().map(userMapper::mapToUserOutDto)
                    .collect(Collectors.toList());
        }
        return userRepository.findByIdIn(ids, pageRequest).stream().map(userMapper::mapToUserOutDto)
                .collect(Collectors.toList());
    }
}
